var sqlite3 = require('sqlite3').verbose()

const DBSOURCE = "exceptions.sqlite"

let db = new sqlite3.Database(DBSOURCE, (err) => {
    if (err) {
      // cannot open database, console log exception
      console.error(err.message)
      throw err
    }else{
        
        db.run(`CREATE TABLE exceptions (
            id INTEGER PRIMARY KEY AUTOINCREMENT,
            message text, 
            stackTrace text, 
            class text, 
            method text, 
            line integer,
            timestamp text,
            app_info text,
            device_info text)`,
        (err) => {
        });  
    }
});

module.exports = db