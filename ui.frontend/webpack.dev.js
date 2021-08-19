const merge = require('webpack-merge');
const AemSyncPlugin = require('aem-sync-webpack-plugin');
const path = require('path');
const common = require('./webpack.common.js');

const aemSyncPath = path.join(
  __dirname,
  '..',
  'ui.apps',
  'src',
  'main',
  'content',
  'jcr_root',
  'apps',
  'premember',
  'clientlibs',
);

module.exports = merge(common, {
  mode: 'development',
  devtool: 'eval-cheap-module-source-map',
  performance: { hints: 'warning' },
  plugins: [
    new AemSyncPlugin({
      targets: ['http://admin:admin@localhost:4502'],
      watchDir: aemSyncPath,
      exclude: ['**/clientlib-base/**', '**/clientlib-grid/**', '**/node_modules/**'],
      pushInterval: 1000, // ms
    }),
  ],
});
