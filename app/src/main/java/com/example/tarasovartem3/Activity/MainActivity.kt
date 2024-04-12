package com.example.tarasovartem3.Activity

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import com.example.tarasovartem3.Viewmodel.PostViewModel
import com.example.tarasovartem3.databinding.ActivityMainBinding

import com.example.Adapter.OnInteractionListener
import com.example.Adapter.PostsAdapter
import com.example.tarasovartem3.Delegation.Post
import com.example.tarasovartem3.NewPostActivityContract.NewPostResultContract
import com.example.tarasovartem3.R


//import kotlin.android.synthetic.main.activity_main.*

class MainActivity<TextView> : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val viewModel: PostViewModel by viewModels()
        val adapter = PostsAdapter(object : OnInteractionListener {
            override fun onVideo(post: Post) {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://www.youtube.com/watch?v=TGM1ZLTkvo4&ab_channel=%D0%94%D0%BD%D0%B5%D0%B2%D0%BD%D0%B8%D0%BA%D0%A1%D0%B5%D1%80%D0%B5%D0%B3%D0%B8%D0%9F%D0%B8%D1%80%D0%B0%D1%82%D0%B0"))
            startActivity(intent)
            }
            override fun onEdit(post: Post) {
                viewModel.edit(post)
            }
            override fun onLike(post: Post) {
                viewModel.likeById(post.id)
            }
            override fun onRemove(post: Post) {
                viewModel.removeById(post.id)
            }
            override fun onShare(post: Post) {
                viewModel.shareById(post.id)
                val intent = Intent().apply {
                    action = Intent.ACTION_SEND
                    putExtra(Intent.EXTRA_TEXT, post.content)
                    type = "text/plain"
                }
                val shareIntent = Intent.createChooser(intent, getString(R.string.postdeli))
                startActivity(shareIntent)
            }
        })
        binding.list.adapter=adapter
        viewModel.data.observe(this) { posts ->
            adapter.submitList(posts)
        }
        val newPostLauncher = registerForActivityResult(NewPostResultContract()) { result ->
            result ?: return@registerForActivityResult
            viewModel.changeContent(result)
            viewModel.save()
        }
        viewModel.edited.observe(this){ post->
            if(post.id == 0){
                return@observe
            }
            newPostLauncher.launch(post.content)
        }
        binding.cancel.setOnClickListener {
            newPostLauncher.launch(null)
//            with(binding.content){
//                viewModel.save()
//                setText("")
//                clearFocus()
//                AndroidUtils.hideKeyboard(this)
//                binding.group.visibility = View.GONE
//            }
        }
        binding.fab.setOnClickListener {
            newPostLauncher.launch(null)
//            with(binding.content){
//                if(text.isNullOrBlank()){
//                    Toast.makeText(
//                        this@MainActivity,
//                        context.getString(R.string.error_empty_content),
//                        Toast.LENGTH_SHORT
//                    ).show()
//                    return@setOnClickListener
//                }
//                viewModel.changeContent(text.toString())
//                viewModel.save()
//                setText("")
//                clearFocus()
//                AndroidUtils.hideKeyboard(this)
//            }
        }
        binding.fab.setOnClickListener{
            newPostLauncher.launch(null)
        }

    }

}



