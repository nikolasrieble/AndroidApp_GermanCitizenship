package com.nrieble.quizapp

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.nrieble.quizapp.domain.QuizItem
import kotlinx.android.synthetic.main.answer_view.view.*

class AnswerAdapter(currentQuizItem: QuizItem, private val context: Context) :
    RecyclerView.Adapter<AnswerAdapter.MyViewHolder>() {

    private var currentQuestion = currentQuizItem

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder.
    // Each data item is just a string in this case that is shown in a TextView.
    class MyViewHolder(var view: View) :
        RecyclerView.ViewHolder(view) {
        var selectionState = view.findViewById<CheckBox>(R.id.checkbox)!!
    }

    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {

        val viewHolder = MyViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.answer_view,
                parent,
                false
            )
        )
        viewHolder.selectionState.setOnCheckedChangeListener { _, isChecked ->
            val index = currentQuestion.options.indexOf(viewHolder.view.checkbox.text)
            if (index >= 0) {
                currentQuestion.selection[index] = isChecked
            }
            if (isChecked) {
                viewHolder.view.setBackgroundColor(
                    ContextCompat.getColor(
                        context,
                        R.color.primaryColor
                    )
                )
            } else {
                viewHolder.view.setBackgroundColor(
                    ContextCompat.getColor(
                        context,
                        R.color.surfaceColor
                    )
                )
            }
        }

        return viewHolder
    }

    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element

        // save selection
        holder.view.checkbox.isChecked = currentQuestion.selection[position]
        // show answer text
        holder.view.checkbox.text = currentQuestion.options[position]

        // if DISCLOSURE, evaluate answers by color
        if (currentQuestion.disclosure) {
            if (currentQuestion.truth[position])
                holder.view.checkbox.setBackgroundColor(
                    ContextCompat.getColor(
                        context,
                        R.color.primaryDarkColor
                    )
                )
            else if (currentQuestion.selection[position] and !currentQuestion.truth[position]) {
                holder.view.checkbox.setBackgroundColor(
                    ContextCompat.getColor(
                        context,
                        R.color.colorFalseAnswer
                    )
                )
            }
        }
    }

    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount() = currentQuestion.options.size
}
