const loaders = require("./loaders");
const path = require('path');
const CopyWebpackPlugin = require('copy-webpack-plugin');
const HtmlWebpackPlugin = require('html-webpack-plugin');
const webpack = require('webpack');

const devBuild = process.env.NODE_ENV === 'dev';

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
        main: [ path.resolve('./src/client/index.js') ]
    },
    output: {
        publicPath: '/',
        path: path.resolve('dist'),
        filename: '[name].js'
    },
    resolve: {
        extensions: [ '.ts', '.tsx', '.js', '.json', '.vue', '.svg' ],
        modules: [ 'src/client', 'src/img', 'src/styles', 'node_modules' ],
        alias: {
            'vue$': 'vue/dist/vue.esm.js'
        }
    },
    devtool: 'source-map', //devBuild ? 'cheap-eval-source-map' : 'source-map',
    plugins: [
        new CopyWebpackPlugin([
                { from: 'src/img', to: 'img' }
        ]),
        new HtmlWebpackPlugin({
            template: './src/html/index.html',
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
