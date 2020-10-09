const mongoose = require('mongoose')
mongoose.connect('mongodb://127.0.0.1:27017/pothholeapp-api',{
    useNewUrlParser:true,
    useCreateIndex:true
})
const holeSchema = new mongoose.Schema({
    image:{type:String,required:true},
    location:{type:String,required:true,trim:true},
    district:{type:String,required:true,trim:true},
    sender:{type:String,required:true}
},{timestamps:true})
const Hole = mongoose.model('Hole',holeSchema)
module.exports=Hole