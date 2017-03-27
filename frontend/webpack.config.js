
const path                  = require('path')
const webpack               = require('webpack')
const rucksack              = require('rucksack-css')
const CopyWebpackPlugin     = require('copy-webpack-plugin')
const HtmlWebpackPlugin     = require('html-webpack-plugin')
const FaviconsWebpackPlugin = require('favicons-webpack-plugin')

module.exports = {
  // The base directory, an absolute path, for resolving entry points and
  // loaders from configuration.
  context: path.join(__dirname, 'client'),

  // The point or points to enter the application. At this point the application
  // starts executing. If an array is passed all items will be executed.
  entry: {
    // The main "app" bundle.
    app: [
      'whatwg-fetch',
      'babel-polyfill',
      'react-hot-loader/patch',
      'webpack-dev-server/client?http://localhost:3000',
      'webpack/hot/only-dev-server',
      './index.js',
    ],
    /*
    // The bundle for libraries that change seldomly.
    vendor: [
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
    */
  },

  // The top-level output key contains set of options instructing webpack on how
  // and where it should output your bundles, assets and anything else you
  // bundle or load with webpack.
  output: {
    path: path.join(__dirname, 'static'),
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
      path.join(__dirname, 'client'),
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
    /*
    new webpack.optimize.CommonsChunkPlugin({
      name: 'vendor',
      filename: 'vendor.bundle.js',
    }),
    */
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
      'card-header-logo.png')),
    new CopyWebpackPlugin([{
      from: path.join(
        __dirname,
        'client',
        'resources',
        'images',
        'og-splash.png'),
    }]),
    new webpack.NamedModulesPlugin(),
    new webpack.HotModuleReplacementPlugin(),
  ],

  // Enables source maps.
  devtool: 'source-map',

  // Development server configuration.
  devServer: {
    contentBase: './client',
    historyApiFallback: true,
    hot: true,
    port: 3000,
    proxy: {
      '/api/*': {
        target:       'http://gauntlet.dev:8080',
        secure:       false,
        pathRewrite:  { '^/api/': '/' },
      },
    },
  },
}
