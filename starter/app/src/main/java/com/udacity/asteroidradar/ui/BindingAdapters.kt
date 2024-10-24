package com.udacity.asteroidradar.ui

import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat.getString
import androidx.databinding.BindingAdapter
import com.udacity.asteroidradar.R

@BindingAdapter("statusIcon")
fun bindAsteroidStatusImage(imageView: ImageView, isHazardous: Boolean) {
    if (isHazardous) {
        imageView.setImageResource(R.drawable.ic_status_potentially_hazardous)
    } else {
        imageView.setImageResource(R.drawable.ic_status_normal)
    }
}

@BindingAdapter("statusContentDesc")
fun bindAsteroidStatusContentDesc(imageView: ImageView, isHazardous: Boolean) {
    val context = imageView.context
    if (isHazardous) {
        imageView.contentDescription = getString(context, R.string.potentially_hazardous)
    } else {
        imageView.contentDescription = getString(context, R.string.not_hazardous)
    }
}

@BindingAdapter("asteroidStatusImage")
fun bindDetailsStatusImage(imageView: ImageView, isHazardous: Boolean) {
    if (isHazardous) {
        imageView.setImageResource(R.drawable.asteroid_hazardous)
    } else {
        imageView.setImageResource(R.drawable.asteroid_safe)
    }
}

@BindingAdapter("astronomicalUnitText")
fun bindTextViewToAstronomicalUnit(textView: TextView, number: Double) {
    val context = textView.context
    textView.text = String.format(context.getString(R.string.astronomical_unit_format), number)
}

@BindingAdapter("kmUnitText")
fun bindTextViewToKmUnit(textView: TextView, number: Double) {
    val context = textView.context
    textView.text = String.format(context.getString(R.string.km_unit_format), number)
}

@BindingAdapter("velocityText")
fun bindTextViewToDisplayVelocity(textView: TextView, number: Double) {
    val context = textView.context
    textView.text = String.format(context.getString(R.string.km_s_unit_format), number)
}
@BindingAdapter("apodText")
fun bindTextViewToApod(textView: TextView, title: String) {
    val context = textView.context
    if(title.isNotEmpty()){
        textView.text = String.format(context.getString(R.string.nasa_picture_of_day_content_description_format), title)
    }else{
        textView.text = context.getString(R.string.this_is_nasa_s_picture_of_day_showing_nothing_yet)
    }
}
@BindingAdapter("apodDesc")
fun bindApodDesc(imageView: ImageView, title: String?) {
    val context = imageView.context
    if (title?.isEmpty() == true) {
        imageView.contentDescription = context.getString(R.string.this_is_nasa_s_picture_of_day_showing_nothing_yet)
    } else {
        imageView.contentDescription = String.format(context.getString(R.string.nasa_picture_of_day_content_description_format), title)
    }
}

