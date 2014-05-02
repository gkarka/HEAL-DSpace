package ntua.lib.dspace.app.webui.servlet;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2005</p>
 * <p>Company: </p>
 * @author not attributable
 * @version 1.0
 */

import java.io.*;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;


import org.dspace.authorize.AuthorizeException;
import org.dspace.authorize.AuthorizeManager;
import org.dspace.content.Bitstream;
import org.dspace.content.Bundle;
import org.dspace.content.Collection;
import org.dspace.content.DCValue;
import org.dspace.content.Item;
import org.dspace.core.Constants;
import org.dspace.core.Context;
import org.dspace.core.LogManager;
import org.dspace.core.Utils;
import org.dspace.handle.HandleManager;
import javax.xml.parsers.*;
import org.xml.sax.*;
import org.w3c.dom.*;
import org.w3c.dom.traversal.*;
import org.apache.xpath.*;
import javax.xml.transform.*;

import ntua.lib.dspace.*;
import ntua.lib.dspace.policy.IPolicy;
import ntua.lib.dspace.toc.Book;

public class NtuaBitstreamServlet extends DSpaceServlet {
	/** log4j category */
	private static Logger log = Logger.getLogger(NtuaBitstreamServlet.class);

	protected void doDSGet(Context context, HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException,
			SQLException, AuthorizeException {
		Bitstream bitstream = null;
		Bitstream tocBitstream = null;
		Bitstream policyBitstream = null;

		// Get the ID from the URL
		String idString = request.getPathInfo();
		String handle = "";
		String sequence = "";
		String pageIdx = "0";

		String dcTitle = "";
		Item item = null;

		if (idString != null) {
			// Remove leading slash
			if (idString.startsWith("/")) {
				idString = idString.substring(1);
			}

			// Remove last slash and filename after it
			int slashIndex = idString.lastIndexOf('/');
			if (slashIndex != -1) {
				pageIdx = idString.substring(slashIndex + 1);
				idString = idString.substring(0, slashIndex);
			}

			// Get bitstream sequence ID
			slashIndex = idString.lastIndexOf('/');
			if (slashIndex != -1) {
				sequence = idString.substring(slashIndex + 1);
				handle = idString.substring(0, slashIndex);
			}

			if (pageIdx.indexOf(".") != -1)
				pageIdx = "0";
			// slashIndex = idString.lastIndexOf('/');
			// if (slashIndex != -1) {
			// pageIdx = idString.substring(slashIndex + 1);
			// }

			// Find the corresponding bitstream
			try {

				item = (Item) HandleManager.resolveToObject(context, handle);

				if (item == null) {
					log.info(LogManager.getHeader(context, "invalid_id",
							"path=" + handle));
					JSPManager
							.showInvalidIDError(request, response, handle, -1);
					return;
				}

				org.dspace.content.DCValue[] dcValues = item.getDC("title",
						null, Item.ANY);
				dcTitle = dcValues[0].value;

				request.setAttribute("item", item);

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

				if (bitstream != null) {
					/* locate TOC XML file (if uploaded by user) */
					String name = bitstream.getName();
					int dotIndex = name.indexOf(".");
					name = name.substring(0, dotIndex);
					String tocXml = name + INtuaDspaceConstants.TOC_FILE_EXTENSION;
	
					found = false;
					for (int i = 0; i < bundles.length && !found; i++) {
						Bitstream[] bitstreams = bundles[i].getBitstreams();
						for (int k = 0; k < bitstreams.length && !found; k++) {
							String bsName = bitstreams[k].getName();
							if (bsName.equals(tocXml)) {
								tocBitstream = bitstreams[k];
								found = true;
							}
						}
					}
	
					/* locate Policy XML file (if uploaded by user) */
					/*
					 * String policyXML = name +
					 * INtuaDspaceConstants.POLICY_FILE_EXTENSION;
					 */
					found = false;
					for (int i = 0; i < bundles.length && !found; i++) {
						Bitstream[] bitstreams = bundles[i].getBitstreams();
						for (int k = 0; k < bitstreams.length && !found; k++) {
							String bsName = bitstreams[k].getName();
							if (bsName
									.endsWith(INtuaDspaceConstants.POLICY_FILE_EXTENSION)) {
								policyBitstream = bitstreams[k];
								found = true;
							}
						}
					}
				}
				// TODO: block item, if no policy found

			} catch (NumberFormatException nfe) {
				// Invalid ID - this will be dealt with below
			}
		}

		// Did we get a bitstream ana a policy?
		if (bitstream != null && policyBitstream != null) {
			log.info(LogManager.getHeader(context, "view_bitstream",
					"bitstream_id=" + bitstream.getID()));

			request.setAttribute(INtuaDspaceConstants.ATTRIBUTE_BITSTREAM,
					bitstream);
			request.setAttribute(INtuaDspaceConstants.ATTRIBUTE_TOCBISTREAM,
					tocBitstream);
			request.setAttribute(
					INtuaDspaceConstants.ATTRIBUTE_POLICYBITSTREAM,
					policyBitstream);
			request.setAttribute(INtuaDspaceConstants.ATTRIBUTE_PAGENUMBER,
					pageIdx);

			IPolicy policy = retrievePolicy(policyBitstream);
			if (policy == null) {
				JSPManager.showAuthorizeError(request, response,
						new AuthorizeException());
			}
			/* use the dc.date.available.field as the initial date */
			DCValue[] dates = item.getDC("date", "available", Item.ANY);
			/*
			 * there should at least be one date.available field. Either way,
			 * use the first one found
			 */
			SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
			String initialDateString = dates[0].value;
			if (initialDateString.contains("T")) {
				initialDateString = initialDateString.substring(0,
						initialDateString.indexOf("T"));
			}

			Date initialDate = null;
			try {
				initialDate = df.parse(initialDateString);
			} catch (ParseException e) {
				log.error("Invalid date.available field");
			}
			boolean isAccessAuthorized = false;
			if (initialDate != null) {
				isAccessAuthorized = PolicyUtil.isAccessAuthorized(policy,
						request.getRemoteAddr(), initialDate);
			}

			log.info(dcTitle + " - access authorized: " + isAccessAuthorized);

			// TODO: check the bitstream type, and take the corresponding action
			String bitstreamName = bitstream.getName();
			if (bitstreamName
					.endsWith(INtuaDspaceConstants.BOOK_FILE_EXTENSION)) {
				/* bitstream is a book, forward to the book display page */
				Book book = null;
				Object tocObj = request.getSession().getAttribute(
						"TOC_" + bitstream.getID());
				if (tocObj != null && tocObj instanceof Book)
					book = (Book) tocObj;
				else {
					book = retrieveToc(bitstream, tocBitstream, item);
					request.getSession().setAttribute(
							"TOC_" + bitstream.getID(), book);
					// request.getSession().setAttribute("TOC_bitstream",
					// tocBitstream.getHandle());
				}

				if (book != null) {
					request.setAttribute("Book", book);
				}
				if (isAccessAuthorized) {
					JSPManager
							.showJSP(request, response, "/ntua/viewIndex.jsp");
				} else {
					JSPManager.showAuthorizeError(request, response,
							new AuthorizeException());
				}
			} else {
				/*
				 * bitstream is another file type, serve it in the regular
				 * fashion
				 */
				if (isAccessAuthorized) {
					// Set the response MIME type
					response
							.setContentType(bitstream.getFormat().getMIMEType());

					// Response length
					response.setHeader("Content-Length", String
							.valueOf(bitstream.getSize()));

					// Pipe the bits
					InputStream is = bitstream.retrieve();

					Utils.bufferedCopy(is, response.getOutputStream());
					is.close();
					response.getOutputStream().flush();
				} else {
					JSPManager.showAuthorizeError(request, response,
							new AuthorizeException());
				}

			}

		} else {
			if (bitstream == null) {
				// No bitstream - we got an invalid ID
				log.info(LogManager.getHeader(context, "view_bitstream",
						"invalid_bitstream_id=" + idString));

				JSPManager.showInvalidIDError(request, response, idString,
						Constants.BITSTREAM);
			}
			if (policyBitstream == null) {
				log.info(dcTitle + " - no policy present");
				JSPManager.showAuthorizeError(request, response,
						new AuthorizeException());
			}
		}
	}

	private Book retrieveToc(Bitstream bitstream, Bitstream tocBitstream,
			Item item) throws AuthorizeException, SQLException, IOException {
		Book book = null;

		log.debug("Retrieving toc...");
		if (tocBitstream != null) {
			InputStream tocStream = tocBitstream.retrieve();
			book = TocUtil.parseTocXml(tocStream, bitstream.retrieve());
		} else {
			InputStream bookstream = bitstream.retrieve();
			book = TocUtil.parseBitstream(bookstream, item);
		}

		log.debug("End retrieving toc...");

		return book;
	}

	private IPolicy retrievePolicy(Bitstream policyBitStream)
			throws IOException, SQLException, AuthorizeException {
		IPolicy policy = null;

		log.debug("Retrieving policy...");
		if (policyBitStream != null) {
			InputStream policyStream = policyBitStream.retrieve();
			policy = PolicyUtil.parsePolicyXml(policyStream);
		} else {
			return null;
		}

		log.debug("End retrieving policy...");
		return policy;
	}

}
