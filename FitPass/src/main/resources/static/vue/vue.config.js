const { defineConfig } = require('@vue/cli-service')
module.exports = defineConfig({
  transpileDependencies: true
})
module.exports = {
  publicPath: '',
  devServer: {
    proxy: 'https://mainsite.com/',
    }
}
