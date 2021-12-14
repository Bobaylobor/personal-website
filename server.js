const express = require('express');
const app = express();
const port = 5000;
const ip = "localhost";

app.use(express.static("public"));

app.listen(port, ip, () => {
    console.log('Server started');
});