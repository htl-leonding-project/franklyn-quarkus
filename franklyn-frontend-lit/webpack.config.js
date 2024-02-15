const HtmlWebpackPlugin = require("html-webpack-plugin");
const path = require("path");
const webpack = require("webpack");

module.exports = (env) => ({
  entry: "./src/index.ts",
  mode: "development",
  module: {
    rules: [
      {
        test: /\.ts?$/,
        use: "ts-loader",
        exclude: /node_modules/,
      },
    ],
  },
  devtool: "cheap-source-map",
  resolve: {
    extensions: [".ts", ".js"],
    alias: {
      Model: path.resolve(__dirname, "src/model"),
    },
  },
  output: {
    filename: "bundle-[fullhash].js",
    path: path.resolve(__dirname, "./target"),
    publicPath: env.BASE_HREF || "/",
  },
  plugins: [
    new HtmlWebpackPlugin({
      template: "index.html",
      scriptLoading: "module",
    }),

    new webpack.EnvironmentPlugin({
      BASE_HREF: env.BASE_HREF || "/",
    }),
  ],
  devServer: {
    static: {
      directory: path.join(__dirname, "/"),
    },
    compress: true,
    port: 4200,
    proxy: {
      "/api": "http://127.0.0.1:8080",
      "/streaming": "http://127.0.0.1:8082",
    },
    historyApiFallback: true,
  },
});
