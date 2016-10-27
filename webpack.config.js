var path = require('path');
var webpack = require('webpack')

var ROOT = path.resolve(__dirname, 'client');
var SRC = path.resolve(ROOT, 'js');
var DEST = path.resolve(__dirname, 'build/resources/main/public/webapp');

module.exports = {
  devtool: 'source-map',
  entry: {
    app: SRC + '/index.js',
  },
  resolve: {
    root: [
      path.resolve(ROOT, 'js'),
      path.resolve(ROOT, 'css'),
      path.resolve(ROOT, 'img')
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
        test: /\.js?$/,
        loaders: ['babel-loader?presets[]=es2015&presets[]=react'],
        include: SRC
      },
      {test: /\.css$/, loader: 'style-loader!css-loader'},
      {test: /\.less$/, loader: 'style!css!less'},
      {test: /\.(woff|woff2)(\?v=\d+\.\d+\.\d+)?$/, loader: 'url?limit=10000&mimetype=application/font-woff'},
      {test: /\.ttf(\?v=\d+\.\d+\.\d+)?$/, loader: 'url?limit=10000&mimetype=application/octet-stream'},
      {test: /\.eot(\?v=\d+\.\d+\.\d+)?$/, loader: 'file'},
      {test: /\.svg(\?v=\d+\.\d+\.\d+)?$/, loader: 'url?limit=10000&mimetype=image/svg+xml'}
    ]
  }
};