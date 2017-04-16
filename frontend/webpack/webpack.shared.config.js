
const path                  = require('path')
const webpack               = require('webpack')
const CopyWebpackPlugin     = require('copy-webpack-plugin')
const HtmlWebpackPlugin     = require('html-webpack-plugin')
const FaviconsWebpackPlugin = require('favicons-webpack-plugin')

module.exports = {
  // The base directory, an absolute path, for resolving entry points and
  // loaders from configuration.
  context: path.join(__dirname, '..', 'client'),

  // The top-level output key contains set of options instructing webpack on how
  // and where it should output your bundles, assets and anything else you
  // bundle or load with webpack.
  output: {
    path: path.join(__dirname, '..', 'static'),
    filename: '[name].bundle.js',
    publicPath: '/',
  },

  // These options determine how the different types of modules within a project
  // will be treated.
  module: {
    // An array of Rules which are matched to requests when modules are created.
    // These rules can modify how the module is created. They can apply loaders
    // to the module, or modify the parser.
    rules: [
      {
        test: /\.html$/,
        use: [
          {
            loader: 'file-loader',
            options: {
              name: '[name].[ext]',
            },
          },
        ],
      },
      {
        test: /\.(png|svg|jpg|mp4)$/,
        use: [
          {
            loader: 'url-loader',
            options: {
              limit: 20000,
              name: '[name].[ext]',
            },
          },
        ],
      },
      {
        test: /\.css$/,
        exclude: /client/,
        use: [
          { loader: 'style-loader' },
          { loader: 'css-loader' },
        ],
      },
      {
        test: /\.js$/,
        exclude: /node_modules/,
        loader: 'babel-loader',
      },
    ],
  },

  // These options change how modules are resolved. webpack provides reasonable
  // defaults, but it is possible to change the resolving in detail. Have a look
  // at Module Resolution for more explanation of how the resolver works.
  resolve: {
    // Tell webpack what directories should be searched when resolving modules.
    modules: [
      path.join(__dirname, '..', 'client'),
      'node_modules',
    ],
    // Automatically resolve certain extensions.
    extensions: [ '.js', '.jsx' ],
    // Create aliases to import or require certain modules more easily.
    alias: {
      // TODO(skeswa): remove the rest of this when the new
      // react-tap-event-plugin comes out.
      'react/lib/EventPluginHub': 'react-dom/lib/EventPluginHub',
      'react/lib/EventConstants': 'react-dom/lib/EventConstants',
      'react/lib/ViewportMetrics': 'react-dom/lib/ViewportMetrics',
      'react/lib/EventPluginUtils': 'react-dom/lib/EventPluginUtils',
      'react/lib/EventPropagators': 'react-dom/lib/EventPropagators',
      'react/lib/SyntheticUIEvent': 'react-dom/lib/SyntheticUIEvent'
    },
  },

  // A list of webpack plugins.
  plugins: [
    new HtmlWebpackPlugin({
      title: 'Quizzical - Conquer SAT Math',
      minify: { collapseWhitespace: true },
      template: path.join(__dirname, '..', 'client', 'index.ejs'),
      description: 'Quizzical helps you prepare for the Math section of the ' +
        'SAT by facilitating quick quizzes to get you the practice you need.'
    }),
    new FaviconsWebpackPlugin(path.join(
      __dirname,
      '..',
      'client',
      'resources',
      'images',
      'logo.png')),
    new CopyWebpackPlugin([{
      from: path.join(
        __dirname,
        '..',
        'client',
        'resources',
        'images',
        'og-splash.png'),
    }]),
  ],
}