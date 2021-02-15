@file:Suppress("DEPRECATION")

package ru.lxx.LearnNumbers.ui.main

import android.graphics.Color
import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.turki.vectoranalogclockview.VectorAnalogClock
import ru.lxx.LearnNumbers.R
import java.util.*


/**
 * A placeholder fragment containing a simple view.
 */
class FragmentAnalogClock : Fragment() {

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
        val root = inflater.inflate(R.layout.fragment_analog_clock, container, false)



        val calendar = Calendar.getInstance()
        calendar.add(Calendar.HOUR, (0 until 12).random())
        calendar.add(Calendar.MINUTE, (0 until 59).random())

        val vectorAnalogClock: VectorAnalogClock = root.findViewById(R.id.analog_clock)

        //customization

        //customization
        vectorAnalogClock.setCalendar(calendar)
            .setDiameterInDp(400.0f)
            .setOpacity(1.0f)
            .setShowSeconds(false)
            .setColor(Color.BLACK)


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
        val imageView_tts: ImageView = root.findViewById(R.id.imageView_tts3)
        imageView_tts.setOnClickListener{
            vectorAnalogClock.calendar.get(Calendar.HOUR)
            val hours = vectorAnalogClock.calendar.get(Calendar.HOUR)
            val mins = vectorAnalogClock.calendar.get(Calendar.MINUTE)
            val toSpeak = resources.getQuantityString(R.plurals.hours, hours, hours) + " " + resources.getQuantityString(R.plurals.minutes, mins, mins)
            if (!mTTS.isSpeaking) {
                mTTS.speak(toSpeak, TextToSpeech.QUEUE_FLUSH, null)
            }
        }

        // Setup random number generator
        val imageView_random: ImageView = root.findViewById(R.id.imageView_random3)
        imageView_random.setOnClickListener{
            val _calendar = Calendar.getInstance()
            _calendar.add(Calendar.HOUR, (0 until 12).random())
            _calendar.add(Calendar.MINUTE, (0 until 59).random())
            vectorAnalogClock.setCalendar(_calendar)
        }
        return root
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
        fun newInstance(sectionNumber: Int): FragmentAnalogClock {
            return FragmentAnalogClock().apply {
                arguments = Bundle().apply {
                    putInt(ARG_SECTION_NUMBER, sectionNumber)
                }
            }
        }
    }
}