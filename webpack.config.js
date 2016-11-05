var path = require('path');
var webpack = require('webpack')

var ROOT = path.resolve(__dirname, 'client');
var SRC = path.resolve(ROOT, 'src');
var DEST = path.resolve(__dirname, 'build/resources/main/public/webapp');

module.exports = {
  devtool: 'source-map',
  entry: {
    app: SRC + '/index.js',
  },
  resolve: {
    root: [
      path.resolve(ROOT, 'public'),
      path.resolve(ROOT, 'src')
    ],
    extensions: ['', '.js', '.jsx']
  },
  output: {
    path: DEST,
    filename: 'bundle.js'
  },
  module: {
    loaders: [
      {
        test: /\.js[x]?$/,
        loader: 'babel-loader',
        include: SRC,
        query:
          {
            presets:['react', 'es2015', 'stage-0']
          }
      },
      {test: /\.css$/, loader: 'style-loader!css-loader'},
      {test: /\.less$/, loader: 'style!css!less'},
      {test: /\.(jpg|png|gif|svg|ico)$/i, loader: "file-loader?name=/images/[name].[ext]"},
    ]
  }
};