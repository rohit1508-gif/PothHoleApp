const express  = require('express')
require('./db/mongoose')
const userrouter = require('./routers/user.js')
const pothholerouter=require('./routers/hole.js')
const app = express()
const port = process.env.PORT
app.use(express.json())
app.use(userrouter)
app.use(pothholerouter)
app.listen(port,()=>{
    console.log('Server setup in Port ' + port)
})