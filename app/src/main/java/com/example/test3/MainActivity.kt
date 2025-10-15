package com.example.test3

import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private lateinit var tvTestContent: TextView

    // 定义菜单项ID常量
    companion object {
        private const val MENU_FONT_SMALL = 1
        private const val MENU_FONT_MEDIUM = 2
        private const val MENU_FONT_LARGE = 3
        private const val MENU_NORMAL = 4
        private const val MENU_COLOR_RED = 5
        private const val MENU_COLOR_BLACK = 6
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // 初始化测试文本
        tvTestContent = findViewById(R.id.tvTestContent)

        // 初始化按钮
        val btnShowDialog = findViewById<Button>(R.id.btnShowDialog)
        val btnToListView = findViewById<Button>(R.id.btnToListView)

        // 设置自定义对话框按钮点击事件
        btnShowDialog.setOnClickListener {
            showLoginDialog()
        }

        // 设置跳转到ListView按钮点击事件
        btnToListView.setOnClickListener {
            val intent = Intent(this, ListViewActivity::class.java)
            startActivity(intent)
        }
    }

    // 动态创建选项菜单
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // 创建字体大小子菜单
        val fontSizeMenu = menu.addSubMenu("字体大小")
        fontSizeMenu.add(0, MENU_FONT_SMALL, 0, "小")
        fontSizeMenu.add(0, MENU_FONT_MEDIUM, 0, "中")
        fontSizeMenu.add(0, MENU_FONT_LARGE, 0, "大")

        // 添加普通菜单项
        menu.add(0, MENU_NORMAL, 0, "普通菜单项")

        // 创建字体颜色子菜单
        val fontColorMenu = menu.addSubMenu("字体颜色")
        fontColorMenu.add(0, MENU_COLOR_RED, 0, "红色")
        fontColorMenu.add(0, MENU_COLOR_BLACK, 0, "黑色")

        return true
    }

    // 处理菜单项点击
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            MENU_FONT_SMALL -> {
                tvTestContent.textSize = 10f
                Toast.makeText(this, "已设置为小号字体(10sp)", Toast.LENGTH_SHORT).show()
                true
            }
            MENU_FONT_MEDIUM -> {
                tvTestContent.textSize = 16f
                Toast.makeText(this, "已设置为中号字体(16sp)", Toast.LENGTH_SHORT).show()
                true
            }
            MENU_FONT_LARGE -> {
                tvTestContent.textSize = 20f
                Toast.makeText(this, "已设置为大号字体(20sp)", Toast.LENGTH_SHORT).show()
                true
            }
            MENU_NORMAL -> {
                Toast.makeText(this, "您点击了普通菜单项", Toast.LENGTH_SHORT).show()
                true
            }
            MENU_COLOR_RED -> {
                tvTestContent.setTextColor(Color.RED)
                Toast.makeText(this, "已设置为红色字体", Toast.LENGTH_SHORT).show()
                true
            }
            MENU_COLOR_BLACK -> {
                tvTestContent.setTextColor(Color.BLACK)
                Toast.makeText(this, "已设置为黑色字体", Toast.LENGTH_SHORT).show()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun showLoginDialog() {
        // 加载自定义布局
        val dialogView = layoutInflater.inflate(R.layout.custom_dialog_layout, null)

        // 初始化视图组件
        val etUsername = dialogView.findViewById<EditText>(R.id.etUsername)
        val etPassword = dialogView.findViewById<EditText>(R.id.etPassword)
        val btnCancel = dialogView.findViewById<Button>(R.id.btnCancel)
        val btnConfirm = dialogView.findViewById<Button>(R.id.btnConfirm)

        // 创建AlertDialog
        val builder = AlertDialog.Builder(this)
        builder.setView(dialogView)

        val dialog = builder.create()
        dialog.setCancelable(true)

        // 设置窗口背景透明
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        // 设置按钮点击事件
        btnCancel.setOnClickListener {
            Toast.makeText(this, "登录已取消", Toast.LENGTH_SHORT).show()
            dialog.dismiss()
        }

        btnConfirm.setOnClickListener {
            val username = etUsername.text.toString().trim()
            val password = etPassword.text.toString().trim()

            if (validateInput(username, password)) {
                Toast.makeText(this, "登录成功！\n用户名: $username", Toast.LENGTH_LONG).show()
                dialog.dismiss()
                handleLogin(username, password)
            }
        }

        // 显示对话框
        dialog.show()
    }

    private fun validateInput(username: String, password: String): Boolean {
        return when {
            username.isEmpty() -> {
                Toast.makeText(this, "请输入用户名", Toast.LENGTH_SHORT).show()
                false
            }
            password.isEmpty() -> {
                Toast.makeText(this, "请输入密码", Toast.LENGTH_SHORT).show()
                false
            }
            password.length < 6 -> {
                Toast.makeText(this, "密码至少6位", Toast.LENGTH_SHORT).show()
                false
            }
            else -> true
        }
    }

    private fun handleLogin(username: String, password: String) {
        println("用户登录 - 用户名: $username, 密码: $password")
        if (username == "admin" && password == "123456") {
            Toast.makeText(this, "管理员登录成功！", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(this, "普通用户登录成功！", Toast.LENGTH_SHORT).show()
        }
    }
}