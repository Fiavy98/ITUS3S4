require('dotenv').config();

const mysql = require('mysql2');

const db = mysql.createConnection({
  host: process.env.DB_HOST || '127.0.0.1',
  port: process.env.DB_PORT || 3306,
  user: process.env.DB_USER || 'root',
  password: process.env.DB_PASSWORD || 'root',
  database: process.env.DB_NAME || 'gestion_poulets'
});

// Expose request() to be compatible with some samples using mssql-style API.
db.request = db.query.bind(db);

db.connect(err => {
  if (err) {
    console.error('Erreur DB:', err);
  } else {
    console.log('MySQL connecté');
  }
});

module.exports = db;
