/** @type {import('next').NextConfig} */
const nextConfig = {
  experimental: {
    appDir: true, // ★これが重要！
  },
  async rewrites() {
    return [
      {
        source: '/api/:path*',
        destination: 'https://toki-nishiyama-project-4-batsukuendo.onrender.com/api/:path*',
      },
    ];
  },
};

module.exports = nextConfig;
