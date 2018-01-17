const loaders = require('./loaders');
const path = require('path');
const CopyWebpackPlugin = require('copy-webpack-plugin');
const HtmlWebpackPlugin = require('html-webpack-plugin');
const UglifyJsPlugin = require('uglifyjs-webpack-plugin');
const webpack = require('webpack');

// Force dev builds until we convert all code to TS, or import some babel stuff ot transpile to es5 instead of es6
const devBuild = true;// process.env.NODE_ENV === 'dev';
console.log(`Starting webpack build with NODE_ENV: ${process.env.NODE_ENV}`);

// Loaders specific to compiling
loaders.push({
    test: /\.tsx?$/,
    enforce: 'pre',
    loader: 'tslint-loader',
    exclude: /node_modules/,
    options: {
        typeCheck: true
    }
});

const config = {
    entry: {
        main: [ path.resolve('./client/index.js') ]
    },
    output: {
        publicPath: '/',
        // path: path.resolve('../../../build/resources/main/static/'),
        path: path.resolve('../resources/static/'),
        filename: '[name].js'
    },
    resolve: {
        extensions: [ '.ts', '.tsx', '.js', '.json', '.vue', '.svg' ],
        modules: [ 'client', 'img', 'styles', 'node_modules' ],
        alias: {
            'vue$': 'vue/dist/vue.esm.js'
        }
    },
    devtool: devBuild ? 'cheap-eval-source-map' : 'source-map',
    plugins: [
        new CopyWebpackPlugin([
            { from: 'img', to: 'img' }
        ]),
        new HtmlWebpackPlugin({
            template: './html/index.html',
            inject: 'body',
            hash: true
        }),
        new webpack.ProvidePlugin({
            $: "jquery",
            jQuery: "jquery",
            "window.jQuery": "jquery"
        }),
        // http://vuejs.github.io/vue-loader/en/workflow/production.html
        new webpack.DefinePlugin({
            'process.env': { // Short-circuits all vue.js warning code in "production" builds
                NODE_ENV: JSON.stringify(process.env.NODE_ENV)
            }
        }),
    ],
    module: {
        rules: loaders
    }
};

if (process.env.NODE_ENV === 'production') {
    config.plugins.push(
        new UglifyJsPlugin({
            uglifyOptions: {
                compress: {
                    warnings: false
                }
            },
            parallel: true
        })
    );
}

module.exports = config;
