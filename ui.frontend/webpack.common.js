'use strict';

const path = require('path');
const webpack = require('webpack');
const MiniCssExtractPlugin = require("mini-css-extract-plugin");
const StyleLintPlugin = require('stylelint-webpack-plugin');
const CopyWebpackPlugin = require('copy-webpack-plugin');
const AemClientlibGeneratorPlugin = require('aem-clientlib-generator-webpack-plugin');
const { CleanWebpackPlugin } = require('clean-webpack-plugin');

const SOURCE_ROOT = path.join(__dirname, '/src');

module.exports = {
  resolve: {
    extensions: ['.js'],
  },
  entry: {
    site: path.join(SOURCE_ROOT, '/scripts/main.js'),
    dependencies: path.join(SOURCE_ROOT, '/scripts/vendors.js'),
  },
  output: {
    filename: chunkData => (chunkData.chunk.name === 'dependencies' ? 'clientlib-dependencies/[name].js' : 'clientlib-site/[name].js'), // eslint-disable-line
    path: path.resolve(__dirname, 'dist'),
  },
  module: {
    rules: [
      {
        enforce: 'pre',
        test: /\.js$/,
        exclude: /node_modules/,
        loader: 'eslint-loader',
        options: {
          emitWarning: true,
          formatter: require('eslint-friendly-formatter'),
        },
      },
      {
        test: /\.js$/,
        exclude: /node_modules\/(?!unity-core\/src\/scripts|ally.js\/src)/,
        use: [
          {
            loader: 'babel-loader',
          },
          {
            loader: 'webpack-import-glob-loader',
            options: {
              url: false,
            },
          },
        ],
      },
      {
        test: /\.scss$/,
        use: [
          MiniCssExtractPlugin.loader,
          {
            loader: 'css-loader',
            options: {
              url: false,
            },
          },
          {
            loader: 'postcss-loader',
            options: {
              plugins() {
                return [
                  require('autoprefixer'),
                ];
              },
            },
          },
          {
            loader: 'sass-loader',
            options: {
              url: false,
            },
          },
          {
            loader: 'webpack-import-glob-loader',
            options: {
              url: false,
            },
          },
        ],
      },
    ],
  },
  plugins: [
    new CleanWebpackPlugin(),
    new webpack.NoEmitOnErrorsPlugin(),
    new MiniCssExtractPlugin({
      filename: 'clientlib-[name]/[name].css',
    }),
    new StyleLintPlugin({
      configFile: '.stylelintrc',
    }),
    new CopyWebpackPlugin([
      {
        from: path.resolve(__dirname, path.join(SOURCE_ROOT, '/resources')),
        to: './clientlib-site/resources',
      },
    ],
    {
      copyUnmodified: true,
    }),
    new AemClientlibGeneratorPlugin(),
  ],
  stats: {
    assetsSort: 'chunks',
    builtAt: true,
    children: false,
    chunkGroups: true,
    chunkOrigins: true,
    colors: false,
    errors: true,
    errorDetails: true,
    env: true,
    modules: false,
    performance: true,
    providedExports: false,
    source: false,
    warnings: true,
  },
};
