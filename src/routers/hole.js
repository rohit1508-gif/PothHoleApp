const express = require('express')
const auth = require('../middleware/auth')
const router = new express.Router()
const Hole = require('../models/hole')
router.post('/holes',async(req,res)=>{
    try{
        const hole = await new Hole(req.body)
        await hole.save()
        res.status(201).send(hole)
    }
    catch(error){res.status(400).send(error)
    console.log(error)}
})
router.get('/holes',auth,async(req,res)=>{
    district=req.user.district
    try{
       const hole = await Hole.find({district})
       res.send(hole)
    }
    catch(error){res.status(500).send(error)}
})
router.delete('/holes/:id',auth,async(req,res)=>{
    _id=req.params.id
    district=req.user.district
    try{
       const hole = await Hole.find({_id,district})
       await hole.remove()
       res.send(hole)
    }
    catch(error){res.status(500).send(error)}
})
module.exports = router