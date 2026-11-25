import { defineConfig } from 'vite'
import react from '@vitejs/plugin-react'

// https://vite.dev/config/
export default defineConfig({
  plugins: [react()],
  server: {
    proxy: Object.fromEntries(
      ['/auth','/auth/me','/auth/csrf','/auth/login','/users','/conversations']
        .map(path => [path, { 
          target: 'http://localhost:8081', 
          changeOrigin: true,
        }])
      )
    },
  },
)
