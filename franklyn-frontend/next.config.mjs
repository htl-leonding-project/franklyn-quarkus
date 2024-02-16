/** @type {import('next').NextConfig} */
const nextConfig = {
  basePath: process.env.NEXT_PUBLIC_BASE_PATH,
  output: "standalone",
  eslint: {
    ignoreDuringBuilds: true,
  },
  publicRuntimeConfig: {
    NEXT_PUBLIC_API_SERVER: process.env.NEXT_PUBLIC_API_SERVER,
    NEXT_PUBLIC_STREAMING_SERVER: process.env.NEXT_PUBLIC_STREAMING_SERVER,
  },
};

export default nextConfig;
