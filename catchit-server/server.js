// create express app (REST pattern)
var express = require("express")
var app = express()
var db = require("./database.js")
// json body parser
var bodyParser = require("body-parser");
app.use(bodyParser.json());

// server port
var HTTP_PORT = 9000 

// start server
app.listen(HTTP_PORT, () => {
    console.log("catchit server running on port %PORT%".replace("%PORT%",HTTP_PORT))
});

// root endpoint
app.get("/", (req, res, next) => {
    res.json({
        "status":1,
        "data":"server is running"
    })
});

// endpoint for fetching all exceptions
app.get("/api/exceptions", (req, res, next) => {
    var sql = "select * from exceptions"
    var params = []
    db.all(sql, params, (err, rows) => {
        if (err) {
          res.status(400).json(
              {"status":-1,
              "data": err.message
            });
        } else {
            res.json({
                "status":1,
                "data":rows
            })
        }
      });
});

// endpoint for inserting multiple exceptions
app.post("/api/exceptions/", (req, res, next) => {
    
    var exceptions = req.body.exceptions;
    var app_info = req.body.appInfo;
    var device_info = req.body.deviceInfo;

    if(exceptions.length == 0) {
        res.json({
            "status":-1,
            "data": "no exceptions to process"
        })
    } else {

        // serialize database operations using serialize function and transaction (begin->commit)
        db.serialize(function() {
            db.run("begin");
          
            for (var i = 0; i < exceptions.length; i++) {
                insertException(exceptions[i], app_info, device_info);
                // if last item
                if(i === exceptions.length-1) {
                    res.json({
                        "status":1
                    })
                }
            }
        
            db.run("commit");
        });
    }
    
})

function insertException(exception, app_info, device_info) {

    var exceptionModel = {
        id: exception.id,
        message: exception.message,
        stacktrace: exception.stackTrace,
        class: exception.canonicalName,
        method: exception.methodName,
        line: exception.lineNumber,
        timestamp: exception.timestamp,
    }

    var sql ='insert into exceptions (id, message, stackTrace, class, method, line, timestamp, app_info, device_info) values (?,?,?,?,?,?,?,?,?)'
    
    var params =
    [
        exceptionModel.id, 
        exceptionModel.message,
        exceptionModel.stacktrace,
        exceptionModel.class,
        exceptionModel.method,
        exceptionModel.line,
        exceptionModel.timestamp,
        JSON.stringify(app_info),
        JSON.stringify(device_info)
    ]

    db.run(sql, params);
}

// fefault response for any other request
app.use(function(req, res){
    res.status(404);
});
