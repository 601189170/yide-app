package com.yyide.chatim.model

data class selectParentStudent(
        val children: List<Children>,
        val classesId: String,
        val id: String,
        val name: String
){
     data class Children(
             val children: List<Children>,
             val id: String,
             val name: String
     )
}

