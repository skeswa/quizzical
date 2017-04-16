
const path            = require('path')
const webpack         = require('webpack')
const rucksack        = require('rucksack-css')
const baseConfig      = require('./webpack.shared.config')
const UglifyJSPlugin  = require('uglifyjs-webpack-plugin')

module.exports = Object.assign({}, baseConfig, {
  // The point or points to enter the application. At this point the application
  // starts executing. If an array is passed all items will be executed.
  entry: {
    app: [
      'whatwg-fetch',
      'babel-polyfill',
      './index.js',
    ],
    vendor: [
      'autobind-decorator',
      'classnames',
      'debug',
      'element-resize-detector',
      'fixed-data-table',
      'flux-standard-action',
      'lodash',
      'material-ui',
      'react',
      'react-dom',
      'react-redux',
      'react-router',
      'react-router-dom',
      'react-router-redux',
      'react-tap-event-plugin',
      'redux',
      'redux-actions',
      'store',
    ],
  },

  // The top-level output key contains set of options instructing webpack on how
  // and where it should output your bundles, assets and anything else you
  // bundle or load with webpack.
  output: Object.assign({}, baseConfig.output, {
    path: path.join(__dirname, '..', 'org.quizzical.frontend.web', 'static'),
  }),

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
            localIdentName: '[hash:base64:6]',
            discardComments: { removeAll: true },
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

    new webpack.optimize.UglifyJsPlugin({
      compress: {
        warnings: false,
        drop_console: false,
      },
      comments: false,
      sourceMap: false,
    }),
    new webpack.DefinePlugin({
      'process.env': { NODE_ENV: JSON.stringify('production') },
    }),
    new webpack.optimize.CommonsChunkPlugin({
      name: 'vendor',
      filename: 'vendor.bundle.js',
      minChunks: Infinity,
    }),
  ],
})
