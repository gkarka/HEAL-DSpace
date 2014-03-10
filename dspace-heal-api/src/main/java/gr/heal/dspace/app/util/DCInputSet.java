/**
 * The contents of this file are subject to the license and copyright
 * detailed in the LICENSE and NOTICE files at the root of the source
 * tree and available online at
 *
 * http://www.dspace.org/license/
 */
package gr.heal.dspace.app.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import gr.heal.dspace.app.util.DCInput;

/**
 * Class representing all DC inputs required for a submission, organized into
 * pages
 * 
 * @author Brian S. Hughes, based on work by Jenny Toves, OCLC
 * @author aanagnostopoulos, updated this for document-type based submission
 * @version $Revision: 5844 $
 */

public class DCInputSet {
	/** name of the input set */
	private String formName = null;
	/** the inputs ordered by page and row position */
//	private DCInput[][] inputPages = null;
	private HashMap<PageInfo, DCInput[]> inputPages = null;

	private int totalPageNum = 0;
	
	/** constructor */
	public DCInputSet(String formName, HashMap pages,
			Map<String, List<String>> listMap) {
		this.formName = formName;
		
		inputPages = new HashMap<PageInfo, DCInput[]>();
		
        Iterator pagesIterator = pages.keySet().iterator();

        while (pagesIterator.hasNext()){

            PageInfo pageInfo = (PageInfo)pagesIterator.next();
            int pn = pageInfo.getPageNum();
            if(pn > totalPageNum){
                totalPageNum = pn;
            }
			Vector page = (Vector)pages.get(pageInfo);
			DCInput[] dcInput = new DCInput[page.size()];
			for ( int j = 0; j < page.size(); j++ )
			{
				dcInput[j] = new DCInput((Map)page.get(j), listMap);
			}
            inputPages.put(pageInfo, dcInput);            
		}
	}

	/**
	 * Return the name of the form that defines this input set
	 * 
	 * @return formName the name of the form
	 */
	public String getFormName() {
		return formName;
	}

	/**
	 * Return the number of pages in this input set
	 * 
	 * @return number of pages
	 */
	public int getNumberPages() {
		return totalPageNum;
	}

	/**
	 * Get all the rows for a page from the form definition
	 * 
	 * @param pageNum
	 *            desired page within set
	 * @param typeName
	 *	          document type name, used for document type based submission
	 * @param addTitleAlternative
	 *            flag to add the additional title row
	 * @param addPublishedBefore
	 *            flag to add the additional published info
	 * 
	 * @return an array containing the page's displayable rows
	 */

	public DCInput[] getPageRows(int pageNum, String typeName, boolean addTitleAlternative,
			boolean addPublishedBefore) {
		List<DCInput> filteredInputs = new ArrayList<DCInput>();
		if (pageNum <= totalPageNum) {
			
			if(typeName == null) typeName = "";
			
            PageInfo pageInfo = new PageInfo(pageNum, typeName);
            PageInfo defaultPageInfo = new PageInfo(pageNum, "");

            DCInput[] page = null;
			
            Iterator pagesIterator = inputPages.keySet().iterator();
            while (pagesIterator.hasNext()){

                PageInfo pi = (PageInfo)pagesIterator.next();
                if(pi.equals(pageInfo)){
                    page = inputPages.get(pageInfo);
                    break;
                }
            }

            if(page == null){
                page = inputPages.get(defaultPageInfo);
                if(page == null){
                    return null;
                }
            }

            
			for (int i = 0; i < page.length; i++) {
				DCInput input = page[i];
				if (doField(input, addTitleAlternative, addPublishedBefore)) {
					filteredInputs.add(input);
				}
			}
		}

		// Convert list into an array
		DCInput[] inputArray = new DCInput[filteredInputs.size()];
		return filteredInputs.toArray(inputArray);
	}

	/**
	 * Does this set of inputs include an alternate title field?
	 * 
	 * @return true if the current set has an alternate title field
	 */
	public boolean isDefinedMultTitles() {
		return isFieldPresent("title.alternative");
	}

	/**
	 * Does this set of inputs include the previously published fields?
	 * 
	 * @return true if the current set has all the prev. published fields
	 */
	public boolean isDefinedPubBefore() {
		return (isFieldPresent("date.issued")
				&& isFieldPresent("identifier.citation") && isFieldPresent("publisher.null"));
	}

	/**
	 * Does the current input set define the named field? Scan through every
	 * field in every page of the input set
	 * 
	 * @return true if the current set has the named field
	 */
	public boolean isFieldPresent(String fieldName) {
        Iterator pageIterator = inputPages.keySet().iterator();
    	while (pageIterator.hasNext())
	    {
    		DCInput[] pageInputs = inputPages.get(pageIterator.next());
    		for (int row = 0; row < pageInputs.length; row++)
    		{
    			String fullName = pageInputs[row].getElement() + "." + 
				              	  pageInputs[row].getQualifier();
    			if (fullName.equals(fieldName))
    			{
    				return true;
    			}
    		}
	    }
    	return false;
	}

	private static boolean doField(DCInput dcf, boolean addTitleAlternative,
			boolean addPublishedBefore) {
		String rowName = dcf.getElement() + "." + dcf.getQualifier();
		if (rowName.equals("title.alternative") && !addTitleAlternative) {
			return false;
		}
		if (rowName.equals("date.issued") && !addPublishedBefore) {
			return false;
		}
		if (rowName.equals("publisher.null") && !addPublishedBefore) {
			return false;
		}
		if (rowName.equals("identifier.citation") && !addPublishedBefore) {
			return false;
		}

		return true;
	}
}
