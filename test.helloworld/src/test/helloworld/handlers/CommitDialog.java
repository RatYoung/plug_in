package org.eclipse.egit.ui.internal.dialogs;

import java.io.IOException;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.Set;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.Assert;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.egit.core.internal.util.ResourceUtil;
import org.eclipse.egit.ui.Activator;
import org.eclipse.egit.ui.UIUtils;
import org.eclipse.egit.ui.internal.CompareUtils;
import org.eclipse.egit.ui.internal.UIIcons;
import org.eclipse.egit.ui.internal.UIText;
import org.eclipse.egit.ui.internal.commit.CommitHelper;
import org.eclipse.egit.ui.internal.commit.CommitMessageHistory;
import org.eclipse.egit.ui.internal.components.CachedCheckboxTreeViewer;
import org.eclipse.egit.ui.internal.decorators.ProblemLabelDecorator;
import org.eclipse.egit.ui.internal.dialogs.CommitItem;
import org.eclipse.egit.ui.internal.dialogs.CommitMessageComponent;
import org.eclipse.egit.ui.internal.dialogs.SpellcheckableMessageArea;
import org.eclipse.egit.ui.internal.dialogs.CommitDialog.1;
import org.eclipse.egit.ui.internal.dialogs.CommitDialog.10;
import org.eclipse.egit.ui.internal.dialogs.CommitDialog.11;
import org.eclipse.egit.ui.internal.dialogs.CommitDialog.12;
import org.eclipse.egit.ui.internal.dialogs.CommitDialog.13;
import org.eclipse.egit.ui.internal.dialogs.CommitDialog.14;
import org.eclipse.egit.ui.internal.dialogs.CommitDialog.15;
import org.eclipse.egit.ui.internal.dialogs.CommitDialog.16;
import org.eclipse.egit.ui.internal.dialogs.CommitDialog.17;
import org.eclipse.egit.ui.internal.dialogs.CommitDialog.18;
import org.eclipse.egit.ui.internal.dialogs.CommitDialog.19;
import org.eclipse.egit.ui.internal.dialogs.CommitDialog.2;
import org.eclipse.egit.ui.internal.dialogs.CommitDialog.20;
import org.eclipse.egit.ui.internal.dialogs.CommitDialog.21;
import org.eclipse.egit.ui.internal.dialogs.CommitDialog.22;
import org.eclipse.egit.ui.internal.dialogs.CommitDialog.23;
import org.eclipse.egit.ui.internal.dialogs.CommitDialog.24;
import org.eclipse.egit.ui.internal.dialogs.CommitDialog.25;
import org.eclipse.egit.ui.internal.dialogs.CommitDialog.26;
import org.eclipse.egit.ui.internal.dialogs.CommitDialog.3;
import org.eclipse.egit.ui.internal.dialogs.CommitDialog.4;
import org.eclipse.egit.ui.internal.dialogs.CommitDialog.5;
import org.eclipse.egit.ui.internal.dialogs.CommitDialog.6;
import org.eclipse.egit.ui.internal.dialogs.CommitDialog.7;
import org.eclipse.egit.ui.internal.dialogs.CommitDialog.8;
import org.eclipse.egit.ui.internal.dialogs.CommitDialog.9;
import org.eclipse.egit.ui.internal.dialogs.CommitDialog.CommitFileContentProvider;
import org.eclipse.egit.ui.internal.dialogs.CommitDialog.CommitItemFilter;
import org.eclipse.egit.ui.internal.dialogs.CommitDialog.CommitItemSelectionListener;
import org.eclipse.egit.ui.internal.dialogs.CommitDialog.CommitPathLabelProvider;
import org.eclipse.egit.ui.internal.dialogs.CommitDialog.CommitStatusLabelProvider;
import org.eclipse.egit.ui.internal.dialogs.CommitDialog.HeaderSelectionListener;
import org.eclipse.egit.ui.internal.dialogs.CommitItem.Order;
import org.eclipse.egit.ui.internal.dialogs.CommitItem.Status;
import org.eclipse.egit.ui.internal.dialogs.CommitMessageComponent.CommitStatus;
import org.eclipse.egit.ui.internal.staging.StagingView;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.IDialogSettings;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.viewers.CellLabelProvider;
import org.eclipse.jface.viewers.ColumnViewerToolTipSupport;
import org.eclipse.jface.viewers.IDecorationContext;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.StructuredViewer;
import org.eclipse.jface.viewers.TreeViewerColumn;
import org.eclipse.jgit.lib.FileMode;
import org.eclipse.jgit.lib.IndexDiff;
import org.eclipse.jgit.lib.ProgressMonitor;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.lib.RepositoryState;
import org.eclipse.jgit.treewalk.FileTreeIterator;
import org.eclipse.jgit.treewalk.filter.PathFilterGroup;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Link;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeColumn;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.Section;

public class CommitDialog extends TitleAreaDialog {
	private static final String SHOW_UNTRACKED_PREF = "CommitDialog.showUntracked";
	private static final String DIALOG_SETTINGS_SECTION_NAME = Activator.getPluginId() + ".COMMIT_DIALOG_SECTION";
	public static final int COMMIT_AND_PUSH_ID = 30;
	FormToolkit toolkit;
	CommitMessageComponent commitMessageComponent;
	SpellcheckableMessageArea commitText;
	Text authorText;
	Text committerText;
	ToolItem amendingItem;
	ToolItem signedOffItem;
	ToolItem changeIdItem;
	ToolItem showUntrackedItem;
	CachedCheckboxTreeViewer filesViewer;
	Section filesSection;
	Button commitButton;
	Button commitAndPushButton;
	Button ignoreErrors;
	ArrayList<CommitItem> items = new ArrayList();
	private String commitMessage = null;
	private String author = null;
	private String committer = null;
	private Set<String> preselectedFiles = Collections.emptySet();
	private boolean preselectAll = false;
	private ArrayList<String> selectedFiles = new ArrayList();
	private boolean amending = false;
	private boolean amendAllowed = true;
	private boolean showUntracked = true;
	private boolean createChangeId = false;
	private boolean allowToChangeSelection = true;
	private Repository repository;
	private boolean isPushRequested = false;

	private static IPreferenceStore getPreferenceStore() {
		return Activator.getDefault().getPreferenceStore();
	}

	public CommitDialog(Shell parentShell) {
		super(parentShell);
	}

	public String getCommitMessage() {
		return this.commitMessage;
	}

	public void setCommitMessage(String s) {
		this.commitMessage = s;
	}

	public Collection<String> getSelectedFiles() {
		return this.selectedFiles;
	}

	public void setPreselectedFiles(Set<String> preselectedFiles) {
		Assert.isNotNull(preselectedFiles);
		this.preselectedFiles = preselectedFiles;
	}

	public void setPreselectAll(boolean preselectAll) {
		this.preselectAll = preselectAll;
	}

	public void setFiles(Repository repository, Set<String> paths, IndexDiff indexDiff) {
      this.repository = repository;
      this.items.clear();
      Iterator arg4 = paths.iterator();

      while(arg4.hasNext()) {
         String path = (String)arg4.next();
         CommitItem item = new CommitItem();
         item.status = getFileStatus(path, indexDiff);
         item.submodule = FileMode.GITLINK == indexDiff.getIndexMode(path);
         item.path = path;
         item.problemSeverity = getProblemSeverity(repository, path);
         this.items.add(item);
      }

      Collections.sort(this.items, new 1(this));
   }

	public String getAuthor() {
		return this.author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public String getCommitter() {
		return this.committer;
	}

	public void setCommitter(String committer) {
		this.committer = committer;
	}

	public boolean isAmending() {
		return this.amending;
	}

	public void setAmending(boolean amending) {
		this.amending = amending;
	}

	public void setAmendAllowed(boolean amendAllowed) {
		this.amendAllowed = amendAllowed;
	}

	public void setAllowToChangeSelection(boolean allowToChangeSelection) {
		this.allowToChangeSelection = allowToChangeSelection;
	}

	public boolean getCreateChangeId() {
		return this.createChangeId;
	}

	public boolean isPushRequested() {
		return this.isPushRequested;
	}

	protected void createButtonsForButtonBar(Composite parent) {
		this.toolkit.adapt(parent, false, false);
		this.commitAndPushButton = this.createButton(parent, 30, UIText.CommitDialog_CommitAndPush, false);
		this.commitButton = this.createButton(parent, 0, UIText.CommitDialog_Commit, true);
		this.createButton(parent, 1, IDialogConstants.CANCEL_LABEL, false);
		this.updateMessage();
	}

	protected void buttonPressed(int buttonId) {
		if (buttonId == 0) {
			this.okPressed();
		} else if (30 == buttonId) {
			MessageDialog test;
			test.openInformation(shell, "test", "hello, world");
			this.isPushRequested = true;
			this.okPressed();
		} else if (1 == buttonId) {
			this.cancelPressed();
		}

	}

	protected Control createButtonBar(Composite parent) {
		this.toolkit.adapt(parent, false, false);
		return super.createButtonBar(parent);
	}

	protected Control createHelpControl(Composite parent) {
      this.toolkit.adapt(parent, false, false);
      Link link = new Link(parent, 524352);
      ++((GridLayout)parent.getLayout()).numColumns;
      link.setLayoutData(new GridData(64));
      link.setText(UIText.CommitDialog_OpenStagingViewLink);
      link.setToolTipText(UIText.CommitDialog_OpenStagingViewToolTip);
      link.addSelectionListener(new 2(this));
      this.toolkit.adapt(link, false, false);
      return link;
   }

	private void openStagingViewLinkClicked() {
		IWorkbenchWindow workbenchWindow = PlatformUI.getWorkbench().getActiveWorkbenchWindow();
		IWorkbenchPage workbenchPage = workbenchWindow.getActivePage();

		try {
			StagingView e = (StagingView) workbenchPage.showView("org.eclipse.egit.ui.StagingView");
			e.reload(this.repository);
			String message = this.commitMessageComponent.getCommitMessage();
			if (message != null && message.length() > 0) {
				e.setCommitMessage(message);
			}

			this.setReturnCode(1);
			this.close();
		} catch (PartInitException arg4) {
			Activator.handleError(UIText.CommitDialog_OpenStagingViewError, arg4, true);
		}

	}

	protected IDialogSettings getDialogBoundsSettings() {
		IDialogSettings settings = Activator.getDefault().getDialogSettings();
		IDialogSettings section = settings.getSection(DIALOG_SETTINGS_SECTION_NAME);
		if (section == null) {
			section = settings.addNewSection(DIALOG_SETTINGS_SECTION_NAME);
		}

		return section;
	}

	protected ToolBar addMessageDropDown(Composite parent) {
      ToolBar dropDownBar = new ToolBar(parent, 8519680);
      ToolItem dropDownItem = new ToolItem(dropDownBar, 8);
      dropDownItem.setImage(PlatformUI.getWorkbench().getSharedImages().getImage("IMG_LCL_RENDERED_VIEW_MENU"));
      Menu menu = new Menu(dropDownBar);
      dropDownItem.addDisposeListener(new 3(this, menu));
      MenuItem preferencesItem = new MenuItem(menu, 8);
      preferencesItem.setText(UIText.CommitDialog_ConfigureLink);
      preferencesItem.addSelectionListener(new 4(this));
      dropDownItem.addSelectionListener(new 5(this, dropDownItem, menu));
      return dropDownBar;
   }

	protected Control createContents(Composite parent) {
      this.toolkit = new FormToolkit(parent.getDisplay());
      parent.addDisposeListener(new 6(this));
      return super.createContents(parent);
   }

	protected Control createDialogArea(Composite parent) {
      Composite container = (Composite)super.createDialogArea(parent);
      parent.getShell().setText(UIText.CommitDialog_CommitChanges);
      container = this.toolkit.createComposite(container);
      GridDataFactory.fillDefaults().grab(true, true).applyTo(container);
      this.toolkit.paintBordersFor(container);
      GridLayoutFactory.swtDefaults().applyTo(container);
      SashForm sashForm = new SashForm(container, 516);
      this.toolkit.adapt(sashForm, true, true);
      sashForm.setLayoutData(GridDataFactory.fillDefaults().grab(true, true).create());
      this.createMessageAndPersonArea(sashForm);
      this.filesSection = this.createFileSection(sashForm);
      sashForm.setWeights(new int[]{50, 50});
      applyDialogFont(container);
      container.pack();
      this.commitText.setFocus();
      Image titleImage = UIIcons.WIZBAN_CONNECT_REPO.createImage();
      UIUtils.hookDisposal(parent, titleImage);
      this.setTitleImage(titleImage);
      this.setTitle(UIText.CommitDialog_Title);
      if(this.ignoreErrors != null) {
         this.setMessage(UIText.CommitDialog_MessageErrors, 2);
      } else {
         this.setMessage(UIText.CommitDialog_Message, 1);
      }

      this.filesViewer.addCheckStateListener(new 7(this));
      this.updateFileSectionText();
      return container;
   }

	private Section createFileSection(Composite container) {
      Section filesSection = this.toolkit.createSection(container, 272);
      GridDataFactory.fillDefaults().grab(true, true).applyTo(filesSection);
      Composite filesArea = this.toolkit.createComposite(filesSection);
      filesSection.setClient(filesArea);
      this.toolkit.paintBordersFor(filesArea);
      GridLayoutFactory.fillDefaults().extendedMargins(2, 2, 2, 2).applyTo(filesArea);
      ToolBar filesToolbar = new ToolBar(filesSection, 8388608);
      filesSection.setTextClient(filesToolbar);
      8 patternFilter = new 8(this);
      patternFilter.setIncludeLeadingWildcard(true);
      9 resourcesTreeComposite = new 9(this, filesArea, this.toolkit, 65536, patternFilter);
      Tree resourcesTree = resourcesTreeComposite.getViewer().getTree();
      resourcesTree.setData("FormWidgetFactory.drawBorder", "treeBorder");
      resourcesTreeComposite.setLayoutData(GridDataFactory.fillDefaults().hint(600, 200).grab(true, true).create());
      resourcesTree.addSelectionListener(new CommitItemSelectionListener(this));
      resourcesTree.setHeaderVisible(true);
      if(getPreferenceStore().getBoolean("checkBeforeCommitting") && getPreferenceStore().getBoolean("blockCommit")) {
         this.ignoreErrors = new Button(resourcesTreeComposite, 32);
         this.ignoreErrors.setText(UIText.CommitDialog_IgnoreErrors);
         this.ignoreErrors.setSelection(false);
         this.ignoreErrors.addSelectionListener(new 10(this));
      }

      TreeColumn statCol = new TreeColumn(resourcesTree, 16384);
      statCol.setText(UIText.CommitDialog_Status);
      statCol.setWidth(150);
      statCol.addSelectionListener(new HeaderSelectionListener(this, Order.ByStatus));
      TreeColumn resourceCol = new TreeColumn(resourcesTree, 16384);
      resourceCol.setText(UIText.CommitDialog_Path);
      resourceCol.setWidth(415);
      resourceCol.addSelectionListener(new HeaderSelectionListener(this, Order.ByFile));
      this.filesViewer = resourcesTreeComposite.getCheckboxTreeViewer();
      (new TreeViewerColumn(this.filesViewer, statCol)).setLabelProvider(createStatusLabelProvider());
      (new TreeViewerColumn(this.filesViewer, resourceCol)).setLabelProvider(new CommitPathLabelProvider());
      ColumnViewerToolTipSupport.enableFor(this.filesViewer);
      this.filesViewer.setContentProvider(new CommitFileContentProvider());
      this.filesViewer.setUseHashlookup(true);
      IDialogSettings settings = Activator.getDefault().getDialogSettings();
      if(settings.get("CommitDialog.showUntracked") != null) {
         this.showUntracked = Boolean.valueOf(settings.get("CommitDialog.showUntracked")).booleanValue() || getPreferenceStore().getBoolean("commit_dialog_include_untracked");
      }

      this.filesViewer.addFilter(new CommitItemFilter(this, (CommitItemFilter)null));
      this.filesViewer.setInput(this.items.toArray());
      MenuManager menuManager = new MenuManager();
      menuManager.setRemoveAllWhenShown(true);
      menuManager.addMenuListener(this.createContextMenuListener());
      this.filesViewer.getTree().setMenu(menuManager.createContextMenu(this.filesViewer.getTree()));
      this.filesViewer.addCheckStateListener(new 11(this));
      this.showUntrackedItem = new ToolItem(filesToolbar, 32);
      Image showUntrackedImage = UIIcons.UNTRACKED_FILE.createImage();
      UIUtils.hookDisposal(this.showUntrackedItem, showUntrackedImage);
      this.showUntrackedItem.setImage(showUntrackedImage);
      this.showUntrackedItem.setToolTipText(UIText.CommitDialog_ShowUntrackedFiles);
      this.showUntrackedItem.setSelection(this.showUntracked);
      this.showUntrackedItem.addSelectionListener(new 12(this));
      ToolItem checkAllItem = new ToolItem(filesToolbar, 8);
      Image checkImage = UIIcons.CHECK_ALL.createImage();
      UIUtils.hookDisposal(checkAllItem, checkImage);
      checkAllItem.setImage(checkImage);
      checkAllItem.setToolTipText(UIText.CommitDialog_SelectAll);
      checkAllItem.addSelectionListener(new 13(this));
      ToolItem uncheckAllItem = new ToolItem(filesToolbar, 8);
      Image uncheckImage = UIIcons.UNCHECK_ALL.createImage();
      UIUtils.hookDisposal(uncheckAllItem, uncheckImage);
      uncheckAllItem.setImage(uncheckImage);
      uncheckAllItem.setToolTipText(UIText.CommitDialog_DeselectAll);
      uncheckAllItem.addSelectionListener(new 14(this));
      if(!this.allowToChangeSelection) {
         this.amendingItem.setSelection(false);
         this.amendingItem.setEnabled(false);
         this.showUntrackedItem.setSelection(false);
         this.showUntrackedItem.setEnabled(false);
         checkAllItem.setEnabled(false);
         uncheckAllItem.setEnabled(false);
         this.filesViewer.addCheckStateListener(new 15(this));
         this.filesViewer.setAllChecked(true);
      } else {
         boolean includeUntracked = getPreferenceStore().getBoolean("commit_dialog_include_untracked");
         Iterator arg18 = this.items.iterator();

         label48:
         while(true) {
            CommitItem item;
            do {
               do {
                  do {
                     if(!arg18.hasNext()) {
                        break label48;
                     }

                     item = (CommitItem)arg18.next();
                  } while(!this.preselectAll && !this.preselectedFiles.contains(item.path));
               } while(item.status == Status.ASSUME_UNCHANGED);
            } while(!includeUntracked && item.status == Status.UNTRACKED);

            this.filesViewer.setChecked(item, true);
         }
      }

      statCol.pack();
      resourceCol.pack();
      return filesSection;
   }

	private void updateCommitButtons(boolean enable) {
		this.commitAndPushButton.setEnabled(enable);
		this.commitButton.setEnabled(enable);
	}

	private Composite createMessageAndPersonArea(Composite container) {
      Composite messageAndPersonArea = this.toolkit.createComposite(container);
      GridDataFactory.fillDefaults().grab(true, true).applyTo(messageAndPersonArea);
      GridLayoutFactory.swtDefaults().margins(0, 0).spacing(0, 0).applyTo(messageAndPersonArea);
      Section messageSection = this.toolkit.createSection(messageAndPersonArea, 272);
      messageSection.setText(UIText.CommitDialog_CommitMessage);
      Composite messageArea = this.toolkit.createComposite(messageSection);
      GridLayoutFactory.fillDefaults().spacing(0, 0).extendedMargins(2, 2, 2, 2).applyTo(messageArea);
      this.toolkit.paintBordersFor(messageArea);
      GridDataFactory.fillDefaults().grab(true, true).applyTo(messageSection);
      GridLayoutFactory.swtDefaults().applyTo(messageSection);
      Composite headerArea = new Composite(messageSection, 0);
      GridLayoutFactory.fillDefaults().spacing(0, 0).numColumns(2).applyTo(headerArea);
      ToolBar messageToolbar = new ToolBar(headerArea, 8388864);
      GridDataFactory.fillDefaults().align(16777224, 4).grab(true, false).applyTo(messageToolbar);
      this.addMessageDropDown(headerArea);
      messageSection.setTextClient(headerArea);
      16 commitProposalProcessor = new 16(this);
      this.commitText = new 17(this, messageArea, this.commitMessage, 0, commitProposalProcessor);
      this.commitText.setData("FormWidgetFactory.drawBorder", "textBorder");
      messageSection.setClient(messageArea);
      Point size = this.commitText.getTextWidget().getSize();
      int minHeight = this.commitText.getTextWidget().getLineHeight() * 3;
      this.commitText.setLayoutData(GridDataFactory.fillDefaults().grab(true, true).hint(size).minSize(size.x, minHeight).align(4, 4).create());
      UIUtils.addBulbDecorator(this.commitText.getTextWidget(), UIText.CommitDialog_ContentAssist);
      Composite personArea = this.toolkit.createComposite(messageAndPersonArea);
      this.toolkit.paintBordersFor(personArea);
      GridLayoutFactory.swtDefaults().numColumns(2).applyTo(personArea);
      GridDataFactory.fillDefaults().grab(true, false).applyTo(personArea);
      this.toolkit.createLabel(personArea, UIText.CommitDialog_Author).setForeground(this.toolkit.getColors().getColor("org.eclipse.ui.forms.TB_TOGGLE"));
      this.authorText = this.toolkit.createText(personArea, (String)null);
      this.authorText.setData("FormWidgetFactory.drawBorder", "textBorder");
      this.authorText.setLayoutData(GridDataFactory.fillDefaults().grab(true, false).create());
      if(this.repository != null && this.repository.getRepositoryState().equals(RepositoryState.CHERRY_PICKING_RESOLVED)) {
         this.authorText.setEnabled(false);
      }

      this.toolkit.createLabel(personArea, UIText.CommitDialog_Committer).setForeground(this.toolkit.getColors().getColor("org.eclipse.ui.forms.TB_TOGGLE"));
      this.committerText = this.toolkit.createText(personArea, (String)null);
      this.committerText.setLayoutData(GridDataFactory.fillDefaults().grab(true, false).create());
      if(this.committer != null) {
         this.committerText.setText(this.committer);
      }

      this.amendingItem = new ToolItem(messageToolbar, 32);
      this.amendingItem.setSelection(this.amending);
      if(this.amending) {
         this.amendingItem.setEnabled(false);
      } else if(!this.amendAllowed) {
         this.amendingItem.setEnabled(false);
      }

      this.amendingItem.setToolTipText(UIText.CommitDialog_AmendPreviousCommit);
      Image amendImage = UIIcons.AMEND_COMMIT.createImage();
      UIUtils.hookDisposal(this.amendingItem, amendImage);
      this.amendingItem.setImage(amendImage);
      this.signedOffItem = new ToolItem(messageToolbar, 32);
      this.signedOffItem.setToolTipText(UIText.CommitDialog_AddSOB);
      Image signedOffImage = UIIcons.SIGNED_OFF.createImage();
      UIUtils.hookDisposal(this.signedOffItem, signedOffImage);
      this.signedOffItem.setImage(signedOffImage);
      this.changeIdItem = new ToolItem(messageToolbar, 32);
      Image changeIdImage = UIIcons.GERRIT.createImage();
      UIUtils.hookDisposal(this.changeIdItem, changeIdImage);
      this.changeIdItem.setImage(changeIdImage);
      this.changeIdItem.setToolTipText(UIText.CommitDialog_AddChangeIdLabel);
      18 listener = new 18(this);
      this.commitMessageComponent = new CommitMessageComponent(this.repository, listener);
      this.commitMessageComponent.enableListeners(false);
      this.commitMessageComponent.setDefaults();
      this.commitMessageComponent.attachControls(this.commitText, this.authorText, this.committerText);
      this.commitMessageComponent.setCommitMessage(this.commitMessage);
      this.commitMessageComponent.setAuthor(this.author);
      this.commitMessageComponent.setCommitter(this.committer);
      this.commitMessageComponent.setAmending(this.amending);
      this.commitMessageComponent.setFilesToCommit(this.getFileList());
      this.amendingItem.addSelectionListener(new 19(this));
      this.changeIdItem.addSelectionListener(new 20(this));
      this.signedOffItem.addSelectionListener(new 21(this));
      this.commitMessageComponent.updateUI();
      this.commitMessageComponent.enableListeners(true);
      return messageAndPersonArea;
   }

	private static CellLabelProvider createStatusLabelProvider() {
      CommitStatusLabelProvider baseProvider = new CommitStatusLabelProvider();
      ProblemLabelDecorator decorator = new ProblemLabelDecorator((StructuredViewer)null);
      return new 22(baseProvider, decorator, (IDecorationContext)null);
   }

	private void updateMessage() {
		if (this.commitButton != null) {
			String message = null;
			boolean type = false;
			boolean ignoreErrorsValue = this.ignoreErrors == null ? true : !this.ignoreErrors.getSelection();
			boolean hasErrorsOrWarnings = getPreferenceStore().getBoolean("checkBeforeCommitting")
					? this.getProblemsSeverity() >= Integer
							.parseInt(getPreferenceStore().getString("warnBeforeCommitting")) && ignoreErrorsValue
					: false;
			int type1;
			if (hasErrorsOrWarnings) {
				message = UIText.CommitDialog_MessageErrors;
				type1 = 2;
			} else {
				String commitBlocked = this.commitMessageComponent.getCommitMessage();
				if (commitBlocked != null && commitBlocked.trim().length() != 0) {
					if (!this.isCommitWithoutFilesAllowed()) {
						message = UIText.CommitDialog_MessageNoFilesSelected;
						type1 = 1;
					} else {
						CommitStatus commitEnabled = this.commitMessageComponent.getStatus();
						message = commitEnabled.getMessage();
						type1 = commitEnabled.getMessageType();
					}
				} else {
					message = UIText.CommitDialog_Message;
					type1 = 1;
				}
			}

			this.setMessage(message, type1);
			boolean commitBlocked1 = getPreferenceStore().getBoolean("checkBeforeCommitting")
					&& getPreferenceStore().getBoolean("blockCommit")
							? this.getProblemsSeverity() >= Integer
									.parseInt(getPreferenceStore().getString("blockCommitCombo")) && ignoreErrorsValue
							: false;
			boolean commitEnabled1 = (type1 == 2 || type1 == 0) && !commitBlocked1;
			this.updateCommitButtons(commitEnabled1);
		}
	}

	private boolean isCommitWithoutFilesAllowed() {
		return this.filesViewer.getCheckedElements().length > 0
				? true
				: (this.amendingItem.getSelection() ? true : CommitHelper.isCommitWithoutFilesAllowed(this.repository));
	}

	private Collection<String> getFileList() {
		ArrayList result = new ArrayList();
		Iterator arg2 = this.items.iterator();

		while (arg2.hasNext()) {
			CommitItem item = (CommitItem) arg2.next();
			result.add(item.path);
		}

		return result;
	}

	private void updateFileSectionText() {
		this.filesSection.setText(MessageFormat.format(UIText.CommitDialog_Files,
				new Object[]{Integer.valueOf(this.filesViewer.getCheckedElements().length),
						Integer.valueOf(this.filesViewer.getTree().getItemCount())}));
	}

	private IMenuListener createContextMenuListener() {
      return new 23(this);
   }

	private Action createCompareAction(CommitItem commitItem) {
      return new 24(this, UIText.CommitDialog_CompareWithHeadRevision, commitItem);
   }

	private Action createAddAction(IStructuredSelection selection) {
      return new 25(this, UIText.CommitDialog_AddFileOnDiskToIndex, selection);
   }

	private IAction createSelectAction(IStructuredSelection selection) {
      return new 26(this, UIText.CommitDialog_SelectForCommit, selection);
   }

	private Status getFileStatus(String path) throws IOException {
		FileTreeIterator fileTreeIterator = new FileTreeIterator(this.repository);
		IndexDiff indexDiff = new IndexDiff(this.repository, "HEAD", fileTreeIterator);
		Set repositoryPaths = Collections.singleton(path);
		indexDiff.setFilter(PathFilterGroup.createFromStrings(repositoryPaths));
		indexDiff.diff((ProgressMonitor) null, 0, 0, "");
		return getFileStatus(path, indexDiff);
	}

	private static Status getFileStatus(String path, IndexDiff indexDiff) {
		return indexDiff.getAssumeUnchanged().contains(path)
				? Status.ASSUME_UNCHANGED
				: (indexDiff.getAdded().contains(path)
						? (indexDiff.getModified().contains(path) ? Status.ADDED_INDEX_DIFF : Status.ADDED)
						: (indexDiff.getChanged().contains(path)
								? (indexDiff.getModified().contains(path)
										? Status.MODIFIED_INDEX_DIFF
										: Status.MODIFIED)
								: (indexDiff.getUntracked().contains(path)
										? (indexDiff.getRemoved().contains(path)
												? Status.REMOVED_UNTRACKED
												: Status.UNTRACKED)
										: (indexDiff.getRemoved().contains(path)
												? Status.REMOVED
												: (indexDiff.getMissing().contains(path)
														? Status.REMOVED_NOT_STAGED
														: (indexDiff.getModified().contains(path)
																? Status.MODIFIED_NOT_STAGED
																: Status.UNKNOWN))))));
	}

	private static int getProblemSeverity(Repository repository, String path) {
		IFile file = ResourceUtil.getFileForLocation(repository, path, false);
		if (file != null) {
			try {
				int severity = file.findMaxProblemSeverity("org.eclipse.core.resources.problemmarker", true, 1);
				return severity;
			} catch (CoreException arg3) {
				;
			}
		}

		return -1;
	}

	protected void okPressed() {
		if (!this.isCommitWithoutFilesAllowed()) {
			MessageDialog.openWarning(this.getShell(), UIText.CommitDialog_ErrorNoItemsSelected,
					UIText.CommitDialog_ErrorNoItemsSelectedToBeCommitted);
		} else if (this.commitMessageComponent.checkCommitInfo()) {
			Object[] checkedElements = this.filesViewer.getCheckedElements();
			this.selectedFiles.clear();
			Object[] arg4 = checkedElements;
			int arg3 = checkedElements.length;

			for (int arg2 = 0; arg2 < arg3; ++arg2) {
				Object settings = arg4[arg2];
				this.selectedFiles.add(((CommitItem) settings).path);
			}

			this.amending = this.commitMessageComponent.isAmending();
			this.commitMessage = this.commitMessageComponent.getCommitMessage();
			this.author = this.commitMessageComponent.getAuthor();
			this.committer = this.commitMessageComponent.getCommitter();
			this.createChangeId = this.changeIdItem.getSelection();
			IDialogSettings arg5 = Activator.getDefault().getDialogSettings();
			arg5.put("CommitDialog.showUntracked", this.showUntracked);
			CommitMessageHistory.saveCommitHistory(this.getCommitMessage());
			super.okPressed();
		}
	}

	private int getProblemsSeverity() {
		int result = -1;
		Object[] checkedElements = this.filesViewer.getCheckedElements();
		ArrayList selectedFiles = new ArrayList();
		Object[] arg6 = checkedElements;
		int arg5 = checkedElements.length;

		for (int arg4 = 0; arg4 < arg5; ++arg4) {
			Object item = arg6[arg4];
			selectedFiles.add(((CommitItem) item).path);
		}

		Iterator arg8 = this.items.iterator();

		while (arg8.hasNext()) {
			CommitItem arg7 = (CommitItem) arg8.next();
			if (arg7.getProblemSeverity() >= 1 && selectedFiles.contains(arg7.path)
					&& result < arg7.getProblemSeverity()) {
				result = arg7.getProblemSeverity();
			}
		}

		return result;
	}

	protected int getShellStyle() {
		return super.getShellStyle() | 16;
	}

	private void compare(CommitItem commitItem) {
		IFile file = this.findFile(commitItem.path);
		if (file != null && ResourceUtil.isSharedWithGit(file.getProject())) {
			CompareUtils.compareHeadWithWorkspace(this.repository, file);
		} else {
			CompareUtils.compareHeadWithWorkingTree(this.repository, commitItem.path);
		}

	}

	private IFile findFile(String path) {
		return ResourceUtil.getFileForLocation(this.repository, path, false);
	}
}