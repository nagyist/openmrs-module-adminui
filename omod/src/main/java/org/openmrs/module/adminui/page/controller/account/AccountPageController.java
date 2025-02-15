/**
 * The contents of this file are subject to the OpenMRS Public License
 * Version 1.0 (the "License"); you may not use this file except in
 * compliance with the License. You may obtain a copy of the License at
 * http://license.openmrs.org
 *
 * Software distributed under the License is distributed on an "AS IS"
 * basis, WITHOUT WARRANTY OF ANY KIND, either express or implied. See the
 * License for the specific language governing rights and limitations
 * under the License.
 *
 * Copyright (C) OpenMRS, LLC.  All Rights Reserved.
 */
package org.openmrs.module.adminui.page.controller.account;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openmrs.Person;
import org.openmrs.api.APIException;
import org.openmrs.api.AdministrationService;
import org.openmrs.api.context.Context;
import org.openmrs.messagesource.MessageSourceService;
import org.openmrs.module.adminui.AdminUiConstants;
import org.openmrs.module.adminui.account.AccountDomainWrapper;
import org.openmrs.module.adminui.account.AccountService;
import org.openmrs.module.adminui.account.AdminUiAccountValidator;
import org.openmrs.module.providermanagement.api.ProviderManagementService;
import org.openmrs.ui.framework.annotation.BindParams;
import org.openmrs.ui.framework.annotation.MethodParam;
import org.openmrs.ui.framework.annotation.SpringBean;
import org.openmrs.ui.framework.page.PageModel;
import org.springframework.context.MessageSource;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

public class AccountPageController {

    protected final Log log = LogFactory.getLog(getClass());

    String suserEnabled;
	String sproviderEnabled;
	int countUsers;

    public AccountDomainWrapper getAccount(@RequestParam(value = "personId", required = false) Person person,
                                           @SpringBean("accountService") AccountService accountService) {

        AccountDomainWrapper account;

        if (person == null) {
            account = accountService.getAccountByPerson(new Person());
        } else {
            account = accountService.getAccountByPerson(person);
            if (account == null)
                throw new APIException("Failed to find user account matching person with id:" + person.getPersonId());
        }

        return account;
    }

    public void get(PageModel model, @MethodParam("getAccount") AccountDomainWrapper account,
                    @SpringBean("accountService") AccountService accountService,
                    @SpringBean("adminService") AdministrationService administrationService,
                    @SpringBean("providerManagementService") ProviderManagementService providerManagementService) {

        model.addAttribute("account", account);
        model.addAttribute("capabilities", accountService.getAllCapabilities());
        model.addAttribute("privilegeLevels", accountService.getAllPrivilegeLevels());
        model.addAttribute("rolePrefix", AdminUiConstants.ROLE_PREFIX_CAPABILITY);
        model.addAttribute("providerRoles", providerManagementService.getAllProviderRoles(false));
    }

    public String post(@MethodParam("getAccount") @BindParams AccountDomainWrapper account, BindingResult errors,
                       @RequestParam(value = "userEnabled", defaultValue = "false") boolean userEnabled,
                       @RequestParam(value = "providerEnabled", defaultValue = "false") boolean providerEnabled,
                       @RequestParam(value = "countTabs", defaultValue = "1") String countTabs,
                       @SpringBean("messageSource") MessageSource messageSource,
                       @SpringBean("messageSourceService") MessageSourceService messageSourceService,
                       @SpringBean("accountService") AccountService accountService,
                       @SpringBean("adminService") AdministrationService administrationService,
                       @SpringBean("providerManagementService") ProviderManagementService providerManagementService,
                       @SpringBean("accountFormValidator") AdminUiAccountValidator accountValidator, 
                       PageModel model,
                       HttpServletRequest request) {

    	countUsers = Integer.parseInt(countTabs);
    	
    	ArrayList<String> username = new ArrayList<String>();
    	ArrayList<String> password = new ArrayList<String>();
    	ArrayList<String> confirmPassword = new ArrayList<String>();
    	ArrayList<String> privilegeLevel = new ArrayList<String>();
    	ArrayList<String[]> roles = new ArrayList<String[]>();
    	
    	if(userEnabled) {
    	
    		for(int i=1 ; i<=countUsers ; i++) {
    			username.add(request.getParameter("user"+i+"_username"));
    			password.add(request.getParameter("user"+i+"_password"));
    			confirmPassword.add(request.getParameter("user"+i+"_confirmPassword"));
    			privilegeLevel.add(request.getParameter("user"+i+"_privilegeLevel"));
    			roles.add(request.getParameterValues("user"+i+"_capabilities"));
    		}
    	
    		account.createRequiredUsers(countUsers);
    		account.setUsernames(username);
    		account.setPasswords(password);
    		account.setConfirmPasswords(confirmPassword);
    		account.setPrivilegeLevels(privilegeLevel);
    		account.setCapabilities(roles);
    		
    		//accountValidator.validate(account, errors);
    	}
    	//request.getSession().setAttribute(EmrConstants.SESSION_ATTRIBUTE_ERROR_MESSAGE, ""+account.getPassword(1));
    	
<<<<<<< HEAD
    	if(userEnabled)
    		suserEnabled = "true";
    	if(providerEnabled)
    		sproviderEnabled = "true";
    		
    	countUsers = Integer.parseInt(countTabs);
    	
    	ArrayList<String> username = new ArrayList<String>();
    	ArrayList<String> password = new ArrayList<String>();
    	ArrayList<String> confirmPassword = new ArrayList<String>();
    	ArrayList<String> privilegeLevel = new ArrayList<String>();
    	ArrayList<String[]> roles = new ArrayList<String[]>();
    	
    	if(userEnabled) {
    	
    		for(int i=1 ; i<=countUsers ; i++) {
    			username.add(request.getParameter("user"+i+"_username"));
    			password.add(request.getParameter("user"+i+"_password"));
    			confirmPassword.add(request.getParameter("user"+i+"_confirmPassword"));
    			privilegeLevel.add(request.getParameter("user"+i+"_privilegeLevel"));
    			roles.add(request.getParameterValues("user"+i+"_capabilities"));
    		}
    	
    		account.createRequiredUsers(countUsers);
    		account.setUsernames(username);
    		account.setPasswords(password);
    		account.setConfirmPasswords(confirmPassword);
    		account.setPrivilegeLevels(privilegeLevel);
    		account.setCapabilities(roles);
    		
    		//accountValidator.validate(account, errors);
    	}
    	//request.getSession().setAttribute(EmrConstants.SESSION_ATTRIBUTE_ERROR_MESSAGE, ""+account.getPassword(1));
    	
=======
>>>>>>> 19b38cd7d43ff3e81718f95df6c7d589e9c2ae69
    	
        if (!errors.hasErrors()) {

            try {
                accountService.saveAccount(account);
                request.getSession().setAttribute(AdminUiConstants.SESSION_ATTRIBUTE_INFO_MESSAGE,
                        messageSourceService.getMessage("adminui.account.saved"));
                request.getSession().setAttribute(AdminUiConstants.SESSION_ATTRIBUTE_TOAST_MESSAGE, "true");

                return "redirect:/adminui/account/manageAccounts.page";
            } catch (Exception e) {
                log.warn("Some error occurred while saving account details:", e);
                request.getSession().setAttribute(AdminUiConstants.SESSION_ATTRIBUTE_ERROR_MESSAGE,
                        messageSourceService.getMessage("adminui.account.error.save.fail "+e.getMessage()+suserEnabled+" "+sproviderEnabled, new Object[]{e.getMessage()}, Context.getLocale()));
            }
        } else {
            sendErrorMessage(errors, messageSource, request);
        }
        
        

        // reload page on error
        // TODO: show password fields toggle should work better

        model.addAttribute("errors", errors);
        model.addAttribute("account", account);
        model.addAttribute("capabilities", accountService.getAllCapabilities());
        model.addAttribute("privilegeLevels", accountService.getAllPrivilegeLevels());
        model.addAttribute("rolePrefix", AdminUiConstants.ROLE_PREFIX_CAPABILITY);
        model.addAttribute("allowedLocales", administrationService.getAllowedLocales());
        model.addAttribute("providerRoles", providerManagementService.getAllProviderRoles(false));

        return "account/account";
	
    }

    private void sendErrorMessage(BindingResult errors, MessageSource messageSource, HttpServletRequest request) {
        List<ObjectError> allErrors = errors.getAllErrors();
        String message = getMessageErrors(messageSource, allErrors);
        request.getSession().setAttribute(AdminUiConstants.SESSION_ATTRIBUTE_ERROR_MESSAGE,
                message);
    }

    private String getMessageErrors(MessageSource messageSource, List<ObjectError> allErrors) {
        String message = "";
        for (ObjectError error : allErrors) {
            Object[] arguments = error.getArguments();
            String errorMessage = messageSource.getMessage(error.getCode(), arguments, Context.getLocale());
            message = message.concat(replaceArguments(errorMessage, arguments).concat("<br>"));
        }
        return message;
    }

    private String replaceArguments(String message, Object[] arguments) {
        if (arguments != null) {
            for (int i = 0; i < arguments.length; i++) {
                String argument = (String) arguments[i];
                message = message.replaceAll("\\{" + i + "\\}", argument);
            }
        }
        return message;
    }

}
