const loaders = require('./loaders');
const path = require('path');
const VueLoaderPlugin = require('vue-loader/lib/plugin');
const CopyWebpackPlugin = require('copy-webpack-plugin');
const HtmlWebpackPlugin = require('html-webpack-plugin');
const WebpackAutoInject = require('webpack-auto-inject-version');
// const UglifyJsPlugin = require('uglifyjs-webpack-plugin');
const webpack = require('webpack');

const devBuild = process.env.NODE_ENV === 'dev';
console.log(`Starting webpack build with NODE_ENV: ${process.env.NODE_ENV}`);

const config = {
    entry: {
        main: [ path.resolve('./client/index.ts') ]
    },
    output: {
        publicPath: '/',
        // path: path.resolve('../../../build/resources/main/static/'),
        path: path.resolve('../resources/static/'),
        filename: '[name].js'
    },
    resolve: {
        extensions: [ '.ts', '.tsx', '.js', '.json', '.svg' ],
        modules: [ 'client', 'img', 'styles', 'node_modules' ],
        alias: {
            'vue$': 'vue/dist/vue.esm.js'
        }
    },
    mode: devBuild ? 'development' : 'production',
    // source-map doesn't seem to be working, see webpack bug reports
    devtool: devBuild ? 'cheap-eval-source-map' : undefined,//'source-map',
    plugins: [
        new VueLoaderPlugin(),
        new CopyWebpackPlugin([
            { from: 'img', to: 'img' }
        ]),
        new HtmlWebpackPlugin({
            template: './html/index.html',
            inject: 'body',
            hash: true
        }),
        new webpack.ProvidePlugin({
        }),
        // http://vuejs.github.io/vue-loader/en/workflow/production.html
        new webpack.DefinePlugin({
            'process.env': { // Short-circuits all vue.js warning code in "production" builds
                NODE_ENV: JSON.stringify(process.env.NODE_ENV)
            }
        }),
        new WebpackAutoInject({
            components: {
                AutoIncreaseVersion: false,
                InjectAsComment: false,
                InjectByTag: true
            },
            componentsOptions: {
                InjectByTag: {
                    fileRegex: /^.*\.js$/,
                    dateFormat: 'mmmm d, yyyy'
                }
            }
        })
    ],
    module: {
        rules: loaders
    }
};

// uglify builds an artifact that throws JS errors in Chrome and Firefox.  Disable for now.
// if (process.env.NODE_ENV === 'production') {
//     console.log('Running uglifyjs for production build');
//     config.plugins.push(
//         new UglifyJsPlugin({
//             uglifyOptions: {
//                 compress: {
//                     warnings: false
//                 }
//             },
//             parallel: true
//         })
//     );
// }

module.exports = config;
