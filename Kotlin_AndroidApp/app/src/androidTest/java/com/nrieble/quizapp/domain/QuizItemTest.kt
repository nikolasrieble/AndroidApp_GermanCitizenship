package com.nrieble.quizapp.domain

import com.nrieble.quizapp.persistence.Answer
import com.nrieble.quizapp.persistence.Question

class QuizItemTest{
    val question = Question(1,"cat", "What is this?", "None")
    val validAnswers = listOf<Answer>(
        Answer(1,1,"correct",1),
        Answer(2,0,"false",1),
        Answer(3,0,"false",1)
    )
    val answersWithNullValues = listOf<Answer>(
        Answer(1,null,"correct",2),
        Answer(2,0,"false",2),
        Answer(3,0,"false",2)
    )
    val answersWithoutTrueOption = listOf<Answer>(
        Answer(1,0,"false",2),
        Answer(2,0,"false",2),
        Answer(3,0,"false",2)
    )
}