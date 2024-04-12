package com.example.tarasovartem3.Repository

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.tarasovartem3.Delegation.Post
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken


class PostRepositorySharedPrefsImpl(contex: Context): PostRepository {
    private val gson = Gson()
    private val prefs = contex.getSharedPreferences("repo", Context.MODE_PRIVATE)
    private val type = TypeToken.getParameterized(List::class.java, Post::class.java).type
    private val key = "posts"
    private var nextId = 1
    private var posts = emptyList<Post>()
    private val data = MutableLiveData(posts)

    init {
        prefs.getString(key, null)?.let {
            posts = gson.fromJson(it, type)
            data.value = posts
        }
    }

    override fun getAll(): LiveData<List<Post>> = data
    override fun likeById(id: Int) {
        TODO("Not yet implemented")
    }

    override fun shareById(id: Int) {
        TODO("Not yet implemented")
    }

    override fun removeById(id: Int) {
        TODO("Not yet implemented")
    }

    private fun sync() {
        with(prefs.edit()) {
            putString(key, gson.toJson(posts))
            apply()
        }
    }

    override fun save(post: Post) {
        if (post.id == 0) {
            posts = listOf(
                post.copy(
                    id = nextId++,
                    author = "Me",
                    likedByMe = false,
                    published = "now",
                    shareByMe = false
                )
            ) + posts
            data.value = posts
            return
        }
        posts = posts.map {
            if (it.id != post.id) it else it.copy(
                content = post.content,
                like = post.like,
                share = post.share
            )
        }
        data.value = posts
        sync()
        fun likeById(id: Int) {
            posts = posts.map {
                if (it.id != id) it else it.copy(
                    likedByMe = !it.likedByMe,
                    like = if (!it.likedByMe) it.like + 1 else it.like - 1
                )
            }
            data.value = posts
            sync()
        }

        fun shareById(id: Int) {
            posts = posts.map {
                if (it.id != id) it else it.copy(shareByMe = !it.shareByMe, share = it.share + 1)
            }
            data.value = posts
            sync()
        }

        fun removeById(id: Int) {
            posts = posts.filter { it.id != id }
            data.value = posts
            sync()
        }
    }
}