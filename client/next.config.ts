import type { NextConfig } from "next";

const nextConfig: NextConfig = {
  async rewrites() {
    return [
      {
        source: '/api/:path*',
        destination: 'https://toki-nishiyama-project-4-batsukuendo.onrender.com/api/:path*',//renderのバックエンドURL
      },
    ];
  },
};

export default nextConfig;
