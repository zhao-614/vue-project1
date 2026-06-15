
// 修复后
import { defineConfig } from 'vite'
import vue from '@vitejs/plugin-vue'
import path from 'path'
import fs from 'fs'

export default defineConfig({
  plugins: [vue()],
  resolve: {
    alias: {
      '@': path.resolve(__dirname, 'src')
    }
  },
  server: {
    fs: {
      allow: [__dirname, 'D:/images']
    },
    proxy: {
      '/api': {
        target: 'http://localhost:8080',
        changeOrigin: true,
        rewrite: (path) => path.replace(/^\/api/, '')
      }
    }
  },
  // 使用 configureServer 配置图片静态资源服务
  configureServer: (server) => {
    server.middlewares.use('/images', (req, res) => {
      // 获取文件名（去掉开头的 /）
      const fileName = req.url.startsWith('/') ? req.url.slice(1) : req.url
      const filePath = path.join('D:/images', fileName)

      fs.readFile(filePath, (err, data) => {
        if (err) {
          res.statusCode = 404
          res.end('Image not found')
          return
        }

        // 根据文件扩展名设置正确的Content-Type
        const ext = path.extname(filePath).toLowerCase()
        const contentType = {
          '.png': 'image/png',
          '.jpg': 'image/jpeg',
          '.jpeg': 'image/jpeg',
          '.gif': 'image/gif',
          '.svg': 'image/svg+xml'
        }[ext] || 'application/octet-stream'

        res.setHeader('Content-Type', contentType)
        res.end(data)
      })
    })
  }
})