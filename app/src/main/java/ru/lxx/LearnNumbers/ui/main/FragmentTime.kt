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
import androidx.lifecycle.ViewModelProviders
import kotlinx.android.synthetic.main.fragment_numbers.*
import ru.lxx.LearnNumbers.R
import java.util.*

/**
 * A placeholder fragment containing a simple view.
 */
class FragmentTime : Fragment() {

    private lateinit var pageViewModel: PageViewModel
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
        val root = inflater.inflate(R.layout.fragment_time, container, false)

        val textView_time: TextView = root.findViewById(R.id.textView_time)
        textView_time.text = randomTime()


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
        val imageView_tts: ImageView = root.findViewById(R.id.imageView_tts2)
        imageView_tts.setOnClickListener{
            val toSpeak = (textView_time.text as String).replace(':', ' ')
            Toast.makeText(activity?.applicationContext, "Speaking $toSpeak", Toast.LENGTH_SHORT).show()
            if (!mTTS.isSpeaking) {
                mTTS.speak(toSpeak, TextToSpeech.QUEUE_FLUSH, null)
            }
        }

        // Setup random number generator
        val imageView_random: ImageView = root.findViewById(R.id.imageView_random2)
        imageView_random.setOnClickListener{
            textView_time.text = randomTime()
        }
        return root
    }

    private fun randomTime(): String{
        val hours: Int = (0 until 23).random()
        val mins: Int = (0 until 59).random()
        return "%d:%02d".format(hours, mins)
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
        fun newInstance(sectionNumber: Int): FragmentTime {
            return FragmentTime().apply {
                arguments = Bundle().apply {
                    putInt(ARG_SECTION_NUMBER, sectionNumber)
                }
            }
        }
    }
}