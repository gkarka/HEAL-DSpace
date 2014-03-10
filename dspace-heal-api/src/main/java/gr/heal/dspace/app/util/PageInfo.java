/*
 * PageInfo.java
 *
 * Version: $Revision: 2491 $
 *
 * Date: $Date: 2008-01-08 11:53:25 -0600 (Tue, 08 Jan 2008) $
 *
 * Copyright (c) 2002-2005, Hewlett-Packard Company and Massachusetts
 * Institute of Technology.  All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are
 * met:
 *
 * - Redistributions of source code must retain the above copyright
 * notice, this list of conditions and the following disclaimer.
 *
 * - Redistributions in binary form must reproduce the above copyright
 * notice, this list of conditions and the following disclaimer in the
 * documentation and/or other materials provided with the distribution.
 *
 * - Neither the name of the Hewlett-Packard Company nor the name of the
 * Massachusetts Institute of Technology nor the names of their
 * contributors may be used to endorse or promote products derived from
 * this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * ``AS IS'' AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT
 * LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR
 * A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT
 * HOLDERS OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT,
 * INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING,
 * BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS
 * OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR
 * TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE
 * USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH
 * DAMAGE.
 */
package gr.heal.dspace.app.util;



import org.apache.log4j.Logger;

/**
 * Information about a page attributes
 * 
 * @author Ying Jin
 * @since 04-24-2009
 */
public class PageInfo
{
    /** log4j logger */
    private static Logger log = Logger.getLogger(PageInfo.class);

    // page number, start from 1
    private int pageNum;

    // information
    private String info;

    /**
     * Constructor
     * @param pageNum
     * @param info
     */
    public PageInfo(int pageNum, String info){
        super();
        this.pageNum = pageNum;
        this.info = info;
    }

    /**
     * @see java.lang.Object#equals(java.lang.Object)
     * @return
     */
    @Override
    public boolean equals(Object obj){
       if (!(obj instanceof PageInfo))
             return false;

       PageInfo pageInfo = (PageInfo) obj;

       if((this.pageNum == pageInfo.getPageNum()) && (this.info.equalsIgnoreCase(pageInfo.getInfo()))){
            return true;
       }
       return false;  
    }

    
    /**
     * @see java.lang.Object#hashCode()
     */
   @Override
   public int hashCode() {
      int hashCode = (Integer.toString(pageNum) + info).hashCode();
      //int hashCode = 1;

      return hashCode;
   }

    /**
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return Integer.toString(pageNum) + ", " + info;
    }

    public int getPageNum(){
        return pageNum;

    }

    public String getInfo(){
        return info;
    }
}

