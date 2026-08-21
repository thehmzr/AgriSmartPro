package com.example.aspro.ui.notifications

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Typeface
import android.os.Bundle
import android.provider.MediaStore
import android.text.SpannableString
import android.text.Spanned
import android.text.style.BulletSpan
import android.text.style.StyleSpan
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.aspro.R
import com.example.aspro.databinding.FragmentNotificationsBinding
import java.util.regex.Pattern

class   NotificationsFragment : Fragment() {

    private var _binding: FragmentNotificationsBinding? = null
    private val binding get() = _binding!!
    private lateinit var chatAdapter: ChatAdapter
    private val chatMessages = mutableListOf<ChatMessage>()
    private val SELECT_IMAGE_REQUEST_CODE = 1001
    private val CAPTURE_IMAGE_REQUEST_CODE = 1002

    private var selectedBitmap: Bitmap? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val notificationsViewModel = ViewModelProvider(this).get(NotificationsViewModel::class.java)

        _binding = FragmentNotificationsBinding.inflate(inflater, container, false)
        val root: View = binding.root

        chatAdapter = ChatAdapter(chatMessages)
        binding.recyclerView.layoutManager = LinearLayoutManager(context)
        binding.recyclerView.adapter = chatAdapter

        binding.buttonSubmit.setOnClickListener {
            val userInput = binding.editTextUserInput.text.toString()
            if (userInput.isNotBlank() || selectedBitmap != null) {
                val userMessage = ChatMessage(userInput, true, selectedBitmap)
                chatAdapter.addMessage(userMessage)
                scrollToBottom()
                binding.editTextUserInput.setText("")
                binding.editTextUserInput.isEnabled = false
                binding.buttonSubmit.isEnabled = false
                binding.buttonSubmit.visibility = View.INVISIBLE
                selectedBitmap = null
                binding.imagePreviewIcon.visibility = View.GONE
                binding.watermarkImageView.visibility = View.GONE

                if (userMessage.bitmap != null) {
                    notificationsViewModel.generateImageResponse(userInput, userMessage.bitmap) { response ->
                        activity?.runOnUiThread {
                            val responseMessage = ChatMessage(response, false)
                            chatAdapter.addMessage(responseMessage)
                            scrollToBottom()
                            binding.editTextUserInput.isEnabled = true
                            binding.buttonSubmit.isEnabled = true
                            binding.buttonSubmit.visibility = View.VISIBLE
                        }
                    }
                } else {
                    notificationsViewModel.generateResponse(userInput) { response ->
                        activity?.runOnUiThread {
                            val responseMessage = ChatMessage(response, false)
                            chatAdapter.addMessage(responseMessage)
                            scrollToBottom()
                            binding.editTextUserInput.isEnabled = true
                            binding.buttonSubmit.isEnabled = true
                            binding.buttonSubmit.visibility = View.VISIBLE
                        }
                    }
                }
            } else {
                Toast.makeText(context, "Please enter a message or select an image", Toast.LENGTH_SHORT).show()
            }
        }

        binding.buttonSelectImage.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            startActivityForResult(intent, SELECT_IMAGE_REQUEST_CODE)
        }

        setHasOptionsMenu(true)

        return root
    }

    override fun onPrepareOptionsMenu(menu: Menu) {
        super.onPrepareOptionsMenu(menu)
        menu.findItem(R.id.action_search)?.isVisible = false
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                SELECT_IMAGE_REQUEST_CODE -> {
                    val imageUri = data?.data
                    val originalBitmap = MediaStore.Images.Media.getBitmap(requireActivity().contentResolver, imageUri)
                    val resizedBitmap = getResizedBitmap(originalBitmap, 200, 200)
                    selectedBitmap = resizedBitmap
                    binding.imagePreviewIcon.visibility = View.VISIBLE
                }
                CAPTURE_IMAGE_REQUEST_CODE -> {
                    val originalBitmap = data?.extras?.get("data") as Bitmap
                    val resizedBitmap = getResizedBitmap(originalBitmap, 200, 200)
                    selectedBitmap = resizedBitmap
                    binding.imagePreviewIcon.visibility = View.VISIBLE
                }
            }
        }
    }

    private fun getResizedBitmap(bitmap: Bitmap, newWidth: Int, newHeight: Int): Bitmap {
        return Bitmap.createScaledBitmap(bitmap, newWidth, newHeight, true)
    }

    private fun parseTextForFormatting(text: String): SpannableString {
        val pattern = Pattern.compile("(\\*\\s)?\\*\\*(.*?)\\*\\*")
        val matcher = pattern.matcher(text)
        val spannable = SpannableString(text)
        val cleanText = StringBuilder(text)
        var offset = 0

        while (matcher.find()) {
            val start = matcher.start(2) - offset
            val end = matcher.end(2) - offset
            val isBullet = matcher.group(1) != null

            if (isBullet) {
                spannable.setSpan(BulletSpan(16), start - 2, start - 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
                cleanText.replace(matcher.start(1) - offset, matcher.start(2) - 2 - offset, "")
                offset += 3
            } else {
                cleanText.replace(matcher.start() - offset, matcher.start(2) - 2 - offset, "")
                offset += 4
            }

            cleanText.replace(matcher.end(2) - 2 - offset, matcher.end() - offset, "")
            offset += 2
            spannable.setSpan(StyleSpan(Typeface.BOLD), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        }

        return SpannableString(cleanText.toString())
    }

    private fun scrollToBottom() {
        binding.recyclerView.scrollToPosition(chatAdapter.itemCount - 1)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
