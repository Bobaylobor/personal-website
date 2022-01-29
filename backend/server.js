const express = require('express');
const path = require('path');
const app = express();
const port = 5000;
const ip = "localhost";

//app.use("/", express.static("../frontend/public/index.html"));
app.use(express.static(path.join(__dirname, '../frontend/public')));

app.listen(port, ip, () => {
    console.log('Server started');
});
