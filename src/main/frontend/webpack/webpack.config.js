const loaders = require('./loaders');
const path = require('path');
const CopyWebpackPlugin = require('copy-webpack-plugin');
const HtmlWebpackPlugin = require('html-webpack-plugin');
const webpack = require('webpack');

const devBuild = true;//process.env.NODE_ENV === 'dev';

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

module.exports = [{
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
    devtool: 'source-map', //devBuild ? 'cheap-eval-source-map' : 'source-map',
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
        })
    ],
    module: {
        rules: loaders//loaders: loaders
    }
}];

if (!devBuild) {
    module.exports[0].plugins.push(new webpack.optimize.UglifyJsPlugin({minimize: true}));
}
