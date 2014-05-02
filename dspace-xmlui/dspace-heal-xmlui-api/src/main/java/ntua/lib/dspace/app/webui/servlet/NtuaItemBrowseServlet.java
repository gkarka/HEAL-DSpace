package ntua.lib.dspace.app.webui.servlet;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2005</p>
 * <p>Company: </p>
 * @author not attributable
 * @version 1.0
 */

import java.io.InputStream;
import java.io.IOException;
import java.sql.SQLException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import org.dspace.app.webui.servlet.DSpaceServlet;
import org.dspace.app.webui.util.JSPManager;
import org.dspace.authorize.AuthorizeException;
import org.dspace.authorize.AuthorizeManager;
import org.dspace.content.Bitstream;
import org.dspace.content.Bundle;
import org.dspace.content.Item;
import org.dspace.core.Constants;
import org.dspace.core.Context;
import org.dspace.core.LogManager;
import org.dspace.core.Utils;
import org.dspace.handle.HandleManager;
import ntua.lib.dspace.TarUtil;

public class NtuaItemBrowseServlet
    extends DSpaceServlet {
  /** log4j category */
  private static Logger log = Logger.getLogger(NtuaItemBrowseServlet.class);

  protected void doDSGet(Context context,
                         HttpServletRequest request,
                         HttpServletResponse response) throws ServletException,
      IOException, SQLException, AuthorizeException {
    Bitstream bitstream = null;

    // Get the ID from the URL
    String idString = request.getPathInfo();
    String handle = "";
    String sequence = "";
    String itemFilename = "0";

    if (idString != null) {
      // Remove leading slash
      if (idString.startsWith("/")) {
        idString = idString.substring(1);
      }

      // Remove last slash and filename after it
      int slashIndex = idString.lastIndexOf('/');


      if (slashIndex != -1) {
        itemFilename = idString.substring(slashIndex + 1);
        idString = idString.substring(0, slashIndex);
      }


      // Get bitstream sequence ID
      slashIndex = idString.lastIndexOf('/');
      if (slashIndex != -1) {
        sequence = idString.substring(slashIndex + 1);
        handle = idString.substring(0, slashIndex);
      }

      // Find the corresponding bitstream
      try {

        Item item = (Item) HandleManager.resolveToObject(context, handle);

        if (item == null) {
          log.info(LogManager.getHeader(context,
                                        "invalid_id",
                                        "path=" + handle));
          JSPManager.showInvalidIDError(request, response, handle, -1);
          return;
        }

        //request.setAttribute("item", item);

        int sid = Integer.parseInt(sequence);
        boolean found = false;

        Bundle[] bundles = item.getBundles();
        for (int i = 0; i < bundles.length && !found; i++) {
          Bitstream[] bitstreams = bundles[i].getBitstreams();
          for (int k = 0; k < bitstreams.length && !found; k++) {
            if (sid == bitstreams[k].getSequenceID()) {
              bitstream = bitstreams[k];
              found = true;
            }
          }
        }
      }
      catch (NumberFormatException nfe) {
        // Invalid ID - this will be dealt with below
      }
    }

    // Did we get a bitstream?
    if (bitstream != null) {
      log.info(LogManager.getHeader(context,
                                    "view_bitstream",
                                    "bitstream_id=" + bitstream.getID()));
/*
      // Set the response MIME type
      response.setContentType(bitstream.getFormat().getMIMEType());

      // Response length
      response.setHeader("Content-Length",
                         String.valueOf(bitstream.getSize()));

      // Pipe the bits
      InputStream is = bitstream.retrieve();

      Utils.bufferedCopy(is, response.getOutputStream());
      is.close();
      response.getOutputStream().flush();
*/
      //request.setAttribute("Bitstream", bitstream);
      //JSPManager.showJSP(request, response, "/ntua/viewIndex.jsp");

      log.debug("Retrieving bitstream...");
      InputStream is = bitstream.retrieve();
      log.debug("Retrieving image...");
      TarUtil tarutil = new TarUtil(is);
      response.setContentType("image/jpeg");
      tarutil.writeImage(itemFilename, response.getOutputStream());
      log.debug("End retrieving image...");
//      String[] images = tarutil.listImages();

//      TarUtil tar = new TarUtil();
//      tar.
    }
    else {
      // No bitstream - we got an invalid ID
      log.info(LogManager.getHeader(context,
                                    "view_bitstream",
                                    "invalid_bitstream_id=" + idString));

      JSPManager.showInvalidIDError(request,
                                    response,
                                    idString,
                                    Constants.BITSTREAM);
    }
  }
}
