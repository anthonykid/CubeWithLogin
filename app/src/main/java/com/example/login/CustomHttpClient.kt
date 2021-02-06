package com.example.login

import org.apache.http.NameValuePair
import org.apache.http.client.HttpClient
import org.apache.http.client.entity.UrlEncodedFormEntity
import org.apache.http.client.methods.HttpGet
import org.apache.http.client.methods.HttpPost
import org.apache.http.conn.params.ConnManagerParams
import org.apache.http.impl.client.DefaultHttpClient
import org.apache.http.params.HttpConnectionParams
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.net.URI
import java.util.*


object CustomHttpClient {
    /** The time it takes for our client to timeout  */
    const val HTTP_TIMEOUT = 30 * 1000 // milliseconds

    /** Single instance of our HttpClient  */
    private var mHttpClient: HttpClient? = null

    /**
     * Get our single instance of our HttpClient object.
     *
     * @return an HttpClient object with connection parameters set
     */
    private val httpClient: HttpClient?
        private get() {
            if (mHttpClient == null) {
                mHttpClient = DefaultHttpClient()
                val params = (mHttpClient as DefaultHttpClient).getParams()
                HttpConnectionParams.setConnectionTimeout(params, HTTP_TIMEOUT)
                HttpConnectionParams.setSoTimeout(params, HTTP_TIMEOUT)
                ConnManagerParams.setTimeout(params, HTTP_TIMEOUT.toLong())
            }
            return mHttpClient
        }

    /**
     * Performs an HTTP Post request to the specified url with the
     * specified parameters.
     *
     * @param url The web address to post the request to
     * @param postParameters The parameters to send via the request
     * @return The result of the request
     * @throws Exception
     */
    @Throws(Exception::class)
    fun executeHttpPost(url: String?, postParameters: ArrayList<NameValuePair?>?): String {
        var `in`: BufferedReader? = null
        return try {
            val client = httpClient
            val request = HttpPost(url)
            val formEntity = UrlEncodedFormEntity(postParameters)
            request.entity = formEntity
            val response = client!!.execute(request)
            `in` = BufferedReader(InputStreamReader(response.entity.content))
            val sb = StringBuffer("")
            var line = ""
            val NL = System.getProperty("line.separator")
            while (`in`.readLine().also { line = it } != null) {
                sb.append(line + NL)
            }
            `in`.close()
            sb.toString()
        } finally {
            if (`in` != null) {
                try {
                    `in`.close()
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }
        }
    }

    /**
     * Performs an HTTP GET request to the specified url.
     *
     * @param url The web address to post the request to
     * @return The result of the request
     * @throws Exception
     */
    @Throws(Exception::class)
    fun executeHttpGet(url: String?): String {
        var `in`: BufferedReader? = null
        return try {
            val client = httpClient
            val request = HttpGet()
            request.uri = URI(url)
            val response = client!!.execute(request)
            `in` = BufferedReader(InputStreamReader(response.entity.content))
            val sb = StringBuffer("")
            var line = ""
            val NL = System.getProperty("line.separator")
            while (`in`.readLine().also { line = it } != null) {
                sb.append(line + NL)
            }
            `in`.close()
            sb.toString()
        } finally {
            if (`in` != null) {
                try {
                    `in`.close()
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }
        }
    }
}