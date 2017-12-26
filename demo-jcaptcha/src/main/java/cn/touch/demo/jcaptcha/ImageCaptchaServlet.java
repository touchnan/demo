package cn.touch.demo.jcaptcha;
import com.octo.captcha.service.CaptchaServiceException;
import com.sun.image.codec.jpeg.JPEGCodec;
import com.sun.image.codec.jpeg.JPEGImageEncoder;
 
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
 
 
public class ImageCaptchaServlet extends HttpServlet {
 
 
    public void init(ServletConfig servletConfig) throws ServletException {
 
        super.init(servletConfig);
 
    }
 
 
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
 
       byte[] captchaChallengeAsJpeg = null;
       // the output stream to render the captcha image as jpeg into
        ByteArrayOutputStream jpegOutputStream = new ByteArrayOutputStream();
        try {
        // get the session id that will identify the generated captcha.
        //the same id must be used to validate the response, the session id is a good candidate!
        String captchaId = req.getSession().getId();
        // call the ImageCaptchaService getChallenge method
            BufferedImage challenge =
                    CaptchaServiceSingleton.getInstance().getImageChallengeForID(captchaId,
                            req.getLocale());
 
            // a jpeg encoder
            JPEGImageEncoder jpegEncoder =
                    JPEGCodec.createJPEGEncoder(jpegOutputStream);
            jpegEncoder.encode(challenge);
        } catch (IllegalArgumentException e) {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        } catch (CaptchaServiceException e) {
            resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            return;
        }
 
        captchaChallengeAsJpeg = jpegOutputStream.toByteArray();
 
        // flush it in the response
        resp.setHeader("Cache-Control", "no-store");
        resp.setHeader("Pragma", "no-cache");
        resp.setDateHeader("Expires", 0);
        resp.setContentType("image/jpeg");
        ServletOutputStream responseOutputStream =
                resp.getOutputStream();
        responseOutputStream.write(captchaChallengeAsJpeg);
        responseOutputStream.flush();
        responseOutputStream.close();
    }


    /* (non-Javadoc)
     * @see javax.servlet.http.HttpServlet#doPost(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
     */
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Boolean isResponseCorrect =Boolean.FALSE;
        //remenber that we need an id to validate!
        String captchaId = req.getSession().getId();
        //retrieve the response
        String response = req.getParameter("j_captcha_response");
        // Call the Service method
         try {
             isResponseCorrect = CaptchaServiceSingleton.getInstance().validateResponseForID(captchaId,
                     response);
         } catch (CaptchaServiceException e) {
              //should not happen, may be thrown if the id is not valid
         }
         
         if (isResponseCorrect) {
             resp.getWriter().write("验证成功");
             return;
         }
         req.getRequestDispatcher("/").forward(req, resp);
//         resp.sendRedirect("/");
         
    }
    
    
}

