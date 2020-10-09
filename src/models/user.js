const mongoose = require('mongoose')
const validator = require('validator')
const bcrypt = require('bcryptjs')
const jwt = require('jsonwebtoken')
mongoose.connect('mongodb://127.0.0.1:27017/pothholeapp-api',{
    useNewUrlParser:true,
    useCreateIndex:true
})
const userSchema = new mongoose.Schema({
    name:{type:String,required:true,trim:true},
    password:{type:String,required:true,trim:true,minlength:7,validate(value){
        if(validator.equals(value,'password')){
            throw new Error('Change your password')
        }
    }},
    district:{type:String,required:true,trim:true},
    tokens:[{token:{type:String,required:true}}]
},
{timestamps:true}
)

userSchema.pre('save',async function(next){
    const user = this
    if(user.isModified('password')){
        user.password = await bcrypt.hash(user.password,8)
    }
    next()
})

userSchema.methods.gettoken=async function(){
    const user =this
    const token = jwt.sign({_id:user._id.toString()},process.env.TOKEN_SECRET)
    user.tokens = user.tokens.concat({token})
    await user.save()
    return token
}

userSchema.statics.findByCridential=async function(name,password){
    const user = await User.findOne({name})
    if(!user){
        throw new Error('login failed')
    }
    const ismatch = await bcrypt.compare(password,user.password)
    if(!ismatch){throw new Error('login failed')}
    return user
}


userSchema.methods.toJSON = function(){
    const user=this
    const userObject = user.toObject()

    delete userObject.password
    delete userObject.tokens
    return userObject
}

const User = mongoose.model('User',userSchema)
module.exports=User