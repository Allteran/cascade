const path = require('path');
const {VueLoaderPlugin} = require('vue-loader');

module.exports = {
  mode: 'development',
  devtool: 'source-map',
  entry: path.join(__dirname, 'src', 'main.js'),
  devServer: {
    static: './dist',
    compress: true,
    port: 8000,
    allowedHosts: [
      'localhost:9000'
    ]
  },
  module: {
    rules: [
      {
        test: /\.js$/,
        exclude: /(node_modules|bower_components)/,
        use: {
          loader: 'babel-loader',
          options: {
            presets: ['@babel/preset-env']
          }
        }
      },
      {
        test: /\.vue$/,
        loader: 'vue-loader'
      }
    ]
  },
  plugins: [
    new VueLoaderPlugin()
  ],
  resolve: {
      modules: [
          path.join(__dirname, 'src'),
          path.join(__dirname, 'node_modules'),
      ],
  }
}