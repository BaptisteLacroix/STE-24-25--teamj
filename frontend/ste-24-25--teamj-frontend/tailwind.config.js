// tailwind.config.js
const { nextui } = require("@nextui-org/react");

export default {
  content: [
    "index.html",
    './src/**/*.{ts,tsx}',
    "./node_modules/@nextui-org/theme/dist/**/*.{js,ts,jsx,tsx}"
  ],
  theme: {
    extend: {},
  },
  darkMode: 'class',
  plugins: [nextui()]
}
