const express = require('express')
const router = new express.Router()
const User = require('../models/user')
const auth = require('../middleware/auth')
const { TokenExpiredError } = require('jsonwebtoken')
router.post('/users',async(req,res)=>{
    const user = new User(req.body)
    try{
    await user.save()
    const token = await user.gettoken()
    res.status(201).send({user,token})}
    catch(error){res.status(400).send("error" + error)
    console.log(error)
}
})
router.post('/users/login',async(req,res)=>{try{
    const user = await User.findByCridential(req.body.name,req.body.password)
    const token = await user.gettoken()
    res.send({user,token})}
    catch(error){res.status(400).send(error)}
})
router.post('/users/logout',auth, async(req,res)=>{
    try{
        req.user.tokens = req.user.tokens.filter((token)=>{
            return token.token!=req.token
        })
        await req.user.save()
        res.send(req.user)
    } catch(e){
        res.status(500).send()
    }
})
module.exports=router