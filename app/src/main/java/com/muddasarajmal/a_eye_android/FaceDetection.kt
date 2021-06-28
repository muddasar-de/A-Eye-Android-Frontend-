package com.muddasarajmal.a_eye_android

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.muddasarajmal.a_eye_android.databinding.FragmentFaceDetectionBinding


class FaceDetection : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding: FragmentFaceDetectionBinding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_face_detection, container, false)
        return binding.root
    }

}