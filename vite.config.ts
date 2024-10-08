import { defineConfig } from 'vite';
import react from '@vitejs/plugin-react-swc';
import tsConfigPaths from 'vite-tsconfig-paths';
import svgr from "vite-plugin-svgr";

export default defineConfig({
  plugins: [svgr(), react(), tsConfigPaths()],
  build: {
    manifest: true,
    outDir: 'target/classes/static',
    rollupOptions: {
      input: "src/main/js/main.tsx",
      output: {
        strict: false,
      }
    },
  }
})

