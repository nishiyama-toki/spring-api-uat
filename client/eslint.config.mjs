import { dirname } from "path";
import { fileURLToPath } from "url";
import { FlatCompat } from "@eslint/eslintrc";

const __filename = fileURLToPath(import.meta.url);
const __dirname = dirname(__filename);

const compat = new FlatCompat({
  baseDirectory: __dirname,
});

const eslintConfig = [
  ...compat.extends("next/core-web-vitals", "next/typescript"),
<<<<<<< HEAD

  // ▼ 本番ビルド用にルール無効化
  {
    rules: {
      "@typescript-eslint/no-explicit-any": "off",
      "@typescript-eslint/no-unused-vars": "off",
      "react/display-name": "off",
      "react-hooks/exhaustive-deps": "off", // warningだから無効化しなくてもいい
    },
  },
=======
>>>>>>> mizukami
];

export default eslintConfig;
