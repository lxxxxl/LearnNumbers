@file:Suppress("DEPRECATION")

package ru.lxx.LearnNumbers.ui.main

import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import ru.lxx.LearnNumbers.R
import java.util.*

/**
 * A placeholder fragment containing a simple view.
 */
class FragmentNumbers : Fragment() {

    private lateinit var pageViewModel: PageViewModel
    private val numbers: Array<String> = arrayOf(" ", "1", "2", "3", "4", "5", "6", "7", "8", "9", "0")

    lateinit var mTTS: TextToSpeech

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        pageViewModel = ViewModelProviders.of(this).get(PageViewModel::class.java).apply {
            setIndex(arguments?.getInt(ARG_SECTION_NUMBER) ?: 1)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_numbers, container, false)

        val textView_num1: TextView = root.findViewById(R.id.textView_num1)
        val textView_num2: TextView = root.findViewById(R.id.textView_num2)
        val textView_num3: TextView = root.findViewById(R.id.textView_num3)

        textView_num1.text = this.numbers[(1 until this.numbers.size-1).random()]
        textView_num2.text = this.numbers[(1 until this.numbers.size-1).random()]
        textView_num3.text = this.numbers[0]

        // Setup Up/Down nums logic
        val imageView_num1_up: ImageView = root.findViewById(R.id.imageView_num1_up)
        imageView_num1_up.setOnClickListener {
            this.numUp(textView = textView_num1)
        }
        val imageView_num1_down: ImageView = root.findViewById(R.id.imageView_num1_down)
        imageView_num1_down.setOnClickListener {
            this.numDown(textView = textView_num1)
        }

        val imageView_num2_up: ImageView = root.findViewById(R.id.imageView_num2_up)
        imageView_num2_up.setOnClickListener {
            this.numUp(textView = textView_num2)
        }
        val imageView_num2_down: ImageView = root.findViewById(R.id.imageView_num2_down)
        imageView_num2_down.setOnClickListener {
            this.numDown(textView = textView_num2)
        }

        val imageView_num3_up: ImageView = root.findViewById(R.id.imageView_num3_up)
        imageView_num3_up.setOnClickListener {
            this.numUp(textView = textView_num3)
        }
        val imageView_num3_down: ImageView = root.findViewById(R.id.imageView_num3_down)
        imageView_num3_down.setOnClickListener {
            this.numDown(textView = textView_num3)
        }

        // Setup Text To Speech engine
        mTTS = TextToSpeech(activity?.applicationContext, TextToSpeech.OnInitListener { status ->
            if (status != TextToSpeech.ERROR){
                //if there is no error then set language
                mTTS.language = Locale.getDefault()
            }
            else{
                Toast.makeText(activity?.applicationContext, "Cannot initialise TTS engine", Toast.LENGTH_SHORT).show()
            }
        })

        // Setup Text To Speech button
        val imageView_tts: ImageView = root.findViewById(R.id.imageView_tts)
        imageView_tts.setOnClickListener{
            val toSpeak = "%s%s%s".format(textView_num3.text, textView_num2.text, textView_num1.text).replace(" ","")
            if (!mTTS.isSpeaking) {
                mTTS.speak(toSpeak, TextToSpeech.QUEUE_FLUSH, null)
            }
        }

        // Setup random number generator
        val imageView_random: ImageView = root.findViewById(R.id.imageView_random)
        imageView_random.setOnClickListener{
            if (textView_num1.text != this.numbers[0])
                textView_num1.text = this.numbers[(1 until this.numbers.size-1).random()]
            if (textView_num2.text != this.numbers[0])
                textView_num2.text = this.numbers[(1 until this.numbers.size-1).random()]
            if (textView_num3.text != this.numbers[0])
                textView_num3.text = this.numbers[(1 until this.numbers.size-1).random()]
        }
        return root
    }

    private fun numUp(textView: TextView){
        val currentNum = textView.text
        val currentIndex = this.numbers.indexOf(currentNum)
        var nextIndex = 0
        if (currentIndex < this.numbers.size-1)
            nextIndex = currentIndex + 1
        textView.setText(this.numbers[nextIndex])
    }

    private fun numDown(textView: TextView){
        val currentNum = textView.text
        val currentIndex = this.numbers.indexOf(currentNum)
        var nextIndex = this.numbers.size-1
        if (currentIndex > 0)
            nextIndex = currentIndex - 1
        textView.text = this.numbers[nextIndex]
    }

    override fun onPause() {
        if (mTTS.isSpeaking){
            //if speaking then stop
            mTTS.stop()
        }
        super.onPause()
    }

    companion object {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private const val ARG_SECTION_NUMBER = "section_number"

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        @JvmStatic
        fun newInstance(sectionNumber: Int): FragmentNumbers {
            return FragmentNumbers().apply {
                arguments = Bundle().apply {
                    putInt(ARG_SECTION_NUMBER, sectionNumber)
                }
            }
        }
    }
}