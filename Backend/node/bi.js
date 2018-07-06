// Conexao com o BD
var mysql = require('mysql');

var con = mysql.createConnection({
  host: "localhost",
  user: "root",
  password: "root",
  database: "SOSUPA"
});

con.connect(function(err) {
  if (err) throw err;
  console.log("Connected!"); 
});


setInterval(function () {
    con.query('SELECT 1');
}, 5000);


module.exports.save_latlong = function (origin, callback) {
  var sql_bi =  "INSERT INTO bi(latlong) VALUE('"+origin+"')";
  con.query(sql_bi, function (err, result) {
    if (err) {
       console.log("Error: Connection with Database!");
       throw err;
    }
  });
}