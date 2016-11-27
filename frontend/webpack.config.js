var rucksack = require('rucksack-css')
var webpack = require('webpack')
var path = require('path')

module.exports = {
  context: path.join(__dirname, './client'),
  entry: {
    jsx: './index.js',
    html: './index.html',
    vendor: [
      'react',
      'react-dom',
      'react-redux',
      'react-router',
      'react-router-redux',
      'redux'
    ]
  },
  output: {
    path: path.join(__dirname, './static'),
    filename: 'bundle.js',
  },
  module: {
    loaders: [
      {
        test: /\.html$/,
        loader: 'file?name=[name].[ext]'
      },
      {
        test: /\.css$/,
        include: /client/,
        loaders: [
          'style-loader',
          'css-loader?modules&sourceMap&importLoaders=1&localIdentName=[local]___[hash:base64:5]',
          'postcss-loader'
        ]
      },
      {
        test: /\.css$/,
        exclude: /client/,
        loader: 'style!css'
      },
      {
        test: /\.(js|jsx)$/,
        exclude: /node_modules/,
        loaders: [
          'react-hot',
          'babel-loader'
        ]
      },
    ],
  },
  resolve: {
    root: path.join(__dirname, './client'),
    extensions: ['', '.js', '.jsx'],
 
    alias: {
      // TODO(skeswa): remove this when react-hot-loader@3 comes out.
      // (https://github.com/gaearon/react-hot-loader/issues/417).
      'react/lib/ReactMount': 'react-dom/lib/ReactMount',

      // TODO(skeswa): remove the rest of this when the new react-tap-event-plugin comes out.
      'react/lib/EventPluginHub': 'react-dom/lib/EventPluginHub',
      'react/lib/EventConstants': 'react-dom/lib/EventConstants',
      'react/lib/ViewportMetrics': 'react-dom/lib/ViewportMetrics',
      'react/lib/EventPluginUtils': 'react-dom/lib/EventPluginUtils',
      'react/lib/EventPropagators': 'react-dom/lib/EventPropagators',
      'react/lib/SyntheticUIEvent': 'react-dom/lib/SyntheticUIEvent'
    }
  },
  postcss: [
    rucksack({
      autoprefixer: true
    })
  ],
  plugins: [
    new webpack.optimize.CommonsChunkPlugin('vendor', 'vendor.bundle.js'),
    new webpack.DefinePlugin({
      'process.env': { NODE_ENV: JSON.stringify(process.env.NODE_ENV || 'development') }
    })
  ],
  devServer: {
    contentBase: './client',
    hot: true,
    proxy: {
      '/api/*': {
        target:       'http://gauntlet.dev:8080',
        secure:       false,
        pathRewrite:  { '^/api/': '/' }
      }
    }
  }
}
