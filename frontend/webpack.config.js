
const path                  = require('path')
const webpack               = require('webpack')
const rucksack              = require('rucksack-css')
const CopyWebpackPlugin     = require('copy-webpack-plugin')
const HtmlWebpackPlugin     = require('html-webpack-plugin')
const FaviconsWebpackPlugin = require('favicons-webpack-plugin')

module.exports = {
  context: path.join(__dirname, './client'),
  entry: {
    jsx: './index.js',
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
    publicPath: '/',
  },
  module: {
    loaders: [
      {
        test: /\.html$/,
        loader: 'file?name=[name].[ext]'
      },
      {
        test: /\.png$/,
        loader: 'url-loader?limit=20000&name=[name].[ext]'
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
      'process.env': {
        NODE_ENV: JSON.stringify(process.env.NODE_ENV || 'development'),
      },
    }),
    new HtmlWebpackPlugin({
      title: 'Quizzical - Conquer SAT Math',
      minify: { collapseWhitespace: true },
      template: path.join(__dirname, 'client', 'index.ejs'),
      description: 'Quizzical helps you prepare for the Math section of the ' +
        'SAT by facilitating quick quizzes to get you the practice you need.'
    }),
    new FaviconsWebpackPlugin(path.join(
      __dirname,
      'client',
      'resources',
      'images',
      'favicon.png')),
    new CopyWebpackPlugin([{
      from: path.join(
        __dirname,
        'client',
        'resources',
        'images',
        'og-splash.png'),
    }])
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
