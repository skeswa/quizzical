
const path       = require('path')
const webpack    = require('webpack')
const rucksack   = require('rucksack-css')
const baseConfig = require('./webpack.shared.config')

module.exports = Object.assign({}, baseConfig, {
  // The point or points to enter the application. At this point the application
  // starts executing. If an array is passed all items will be executed.
  entry: {
    app: [
      'whatwg-fetch',
      'babel-polyfill',
      'react-hot-loader/patch',
      'webpack-dev-server/client?http://localhost:3000',
      'webpack/hot/only-dev-server',
      './index.js',
    ],
  },

  // These options determine how the different types of modules within a project
  // will be treated.
  module: Object.assign({}, baseConfig.module, {
    rules: [...baseConfig.module.rules, {
      test: /\.css$/,
      include: /client/,
      use: [
        { loader: 'style-loader' },
        {
          loader: 'css-loader',
          options: {
            root: '.',
            modules: true,
            importLoaders: 1,
            localIdentName: '[local]___[hash:base64:5]',
          },
        },
        {
          loader: 'postcss-loader',
          options: {
            plugins: () => [ rucksack({ autoprefixer: true }) ],
          },
        },
      ],
    }],
  }),

  // A list of webpack plugins.
  plugins: [
    ...baseConfig.plugins,

    new webpack.DefinePlugin({
      'process.env': { NODE_ENV: JSON.stringify('development') },
    }),
    new webpack.HotModuleReplacementPlugin(),
  ],

  // Enables source maps.
  devtool: 'source-map',

  // Development server configuration.
  devServer: {
    contentBase: path.join(__dirname, '..', 'client'),
    historyApiFallback: true,
    hot: true,
    inline: true,
    port: 3000,
    proxy: {
      '/api/*': {
        target:       'http://gauntlet.dev:8080',
        secure:       false,
        pathRewrite:  { '^/api/': '/' },
      },
    },
  },
})
