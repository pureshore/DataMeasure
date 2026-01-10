function getQueryParam(name) {
        const urlParams = new URLSearchParams(window.location.search);
        return urlParams.get(name);
    }
// 从后端API获取测试用例数据
    async function fetchTestCases(jobId) {
        try {
            // 在实际应用中，这里应该替换为真实的后端API地址
             const response = await fetch(`/api/testcases?jobId=${jobId}`);
             const data = await response.json();
             return data;
        } catch (error) {
            console.error('获取测试用例数据失败:', error);
            return [];
        }
    }

    // 页面加载完成后初始化
        document.addEventListener('DOMContentLoaded', async function() {
            // 获取jobId
            const jobId = getQueryParam('jobId') || '未指定';
            document.getElementById('jobIdDisplay').textContent = `Job ID: ${jobId}`;

            // 显示加载状态
            document.getElementById('testCasesBody').innerHTML = `
                <tr>
                    <td colspan="9" class="text-center py-5">
                        <div class="spinner-border text-primary" role="status">
                            <span class="visually-hidden">加载中...</span>
                        </div>
                        <p class="mt-3">正在加载测试用例数据...</p>
                    </td>
                </tr>
            `;

            // 获取数据
            const testCases = await fetchTestCases(jobId);

            // 存储原始数据
            window.originalTestCases = testCases;

            // 渲染表格
            renderTestCases(testCases);

            // 更新统计信息
            updateStats(testCases);

            // 添加筛选器事件监听
            document.getElementById('statusFilter').addEventListener('change', filterTable);
            document.getElementById('itemFilter').addEventListener('change', filterTable);
            document.getElementById('searchInput').addEventListener('input', filterTable);
        });
    // HTML属性转义函数
    function escapeForHtmlAttribute(text) {
        return text
            .replace(/'/g, "&#39;")    // 单引号
            .replace(/"/g, "&quot;")   // 双引号
            .replace(/</g, "&lt;")     // 小于号
            .replace(/>/g, "&gt;")     // 大于号
            .replace(/&/g, "&amp;");   // &符号
    }

    // 渲染测试用例表格
    function renderTestCases(cases) {
        const tbody = document.getElementById('testCasesBody');
        const emptyState = document.getElementById('emptyState');

        tbody.innerHTML = '';

        if (cases.length === 0) {
            emptyState.style.display = 'block';
            return;
        }

        emptyState.style.display = 'none';

        cases.forEach(testCase => {
            const row = document.createElement('tr');

            // 测试项标签
            let itemBadge = '';
            switch(testCase.testItem) {
                case 'contact':
                    itemBadge = `<span class="badge bg-info">${testCase.testItem}</span>`;
                    break;
                case 'pushType':
                    itemBadge = `<span class="badge bg-primary">${testCase.testItem}</span>`;
                    break;
                case 'accessToken':
                    itemBadge = `<span class="badge bg-warning text-dark">${testCase.testItem}</span>`;
                    break;
                case 'startTime':
                    itemBadge = `<span class="badge bg-success">${testCase.testItem}</span>`;
                    break;
                default:
                    itemBadge = `<span class="badge bg-secondary">${testCase.testItem}</span>`;
            }

            // 状态标签
            let statusBadge = '';
            switch(testCase.status) {
                case 'PASS':
                    statusBadge = `<span class="status-badge status-pass">${testCase.status}</span>`;
                    break;
                case 'FAIL':
                    statusBadge = `<span class="status-badge status-fail">${testCase.status}</span>`;
                    break;
                case 'BLOCKED':
                    statusBadge = `<span class="status-badge status-blocked">${testCase.status}</span>`;
                    break;
            }

            // 预期状态码标签
            let expectCodeBadge = '';
            switch(testCase.expectCode) {
                case 200:
                    expectCodeBadge = `<span class="badge bg-success">${testCase.expectCode}</span>`;
                    break;
                case 400:
                    expectCodeBadge = `<span class="badge bg-secondary">${testCase.expectCode}</span>`;
                    break;
                case 401:
                    expectCodeBadge = `<span class="badge bg-warning text-dark">${testCase.expectCode}</span>`;
                    break;
                default:
                    expectCodeBadge = `<span class="badge bg-info">${testCase.expectCode}</span>`;
            }

            // 实际状态码标签
            let actualCodeBadge = '';
            switch(testCase.actualCode) {
                case 200:
                    actualCodeBadge = `<span class="badge bg-success">${testCase.actualCode}</span>`;
                    break;
                case 400:
                    actualCodeBadge = `<span class="badge bg-secondary">${testCase.actualCode}</span>`;
                    break;
                case 500:
                    actualCodeBadge = `<span class="badge bg-danger">${testCase.actualCode}</span>`;
                    break;
                default:
                    actualCodeBadge = `<span class="badge bg-info">${testCase.actualCode}</span>`;
            }
            // 处理测试结果文本（限制长度）
            const testResult = testCase.result || '';
            const truncatedResult = testResult.length > 50 ?
                testResult.substring(0, 50) + '...' :
                testResult;
            const escapedData = escapeForHtmlAttribute(testCase.caseData);
            const idCell = `
                <td>
                    <div class="d-flex align-items-center gap-2">
                        <strong>${testCase.id}</strong>
                        <button class="btn btn-sm btn-outline-primary btn-edit-id"
                                onclick="openEditIdModal(${testCase.id})"
                                title="编辑测试用例">
                            <i class="bi bi-pencil"></i>
                        </button>
                    </div>
                </td>
            `;

            row.innerHTML = `
                ${idCell}
                <td>${itemBadge}</td>
                <td>${testCase.caseName}</td>
                <td class="case-data-cell">
                    <div class="case-data-display" data-fulltext="${escapedData}">
                        ${testCase.caseData}
                    </div>
                    <button class="btn-edit" onclick="enableEdit(${testCase.id})">
                        <i class="bi bi-pencil"></i> 编辑
                    </button>
                </td>
                <td>${statusBadge}</td>
                <td>${expectCodeBadge}</td>
                <td>${actualCodeBadge}</td>
                <td>
                    <div class="result-container">
                        <span class="truncate-text">
                            ${truncatedResult}
                        </span>
                        <button class="copy-btn" title="复制全部内容">
                            <i class="bi bi-clipboard"></i>
                        </button>
                    </div>
                </td>
                <td><small>${testCase.updateTime}</small></td>
            `;
            tbody.appendChild(row);

        // 添加复制按钮事件监听器 - 使用闭包捕获testCase变量
        const copyBtn = row.querySelector('.copy-btn');
        copyBtn.addEventListener('click', (function(result) {
            return function(e) {
                e.stopPropagation();
                copyToClipboard(result);

                // 添加复制成功反馈
                this.classList.add('copied');
                this.innerHTML = '<i class="bi bi-check"></i>';

                // 2秒后恢复原状
                setTimeout(() => {
                    this.classList.remove('copied');
                    this.innerHTML = '<i class="bi bi-clipboard"></i>';
                }, 2000);
            };
        })(testCase.result));
        });
    }

    // 启用编辑模式
        function enableEdit(caseId) {
            const cell = document.querySelector(`tr:has(button[onclick="enableEdit(${caseId})"]) .case-data-cell`);
            const displayDiv = cell.querySelector('.case-data-display');
            const editButton = cell.querySelector('.btn-edit');
            const originalData = displayDiv.getAttribute('data-fulltext');
            // 安全地转义数据
            const escapedData = escapeForHtmlAttribute(originalData);

            // 创建编辑区域
            const editArea = document.createElement('div');
            editArea.innerHTML = `
                <textarea class="edit-input">${originalData}</textarea>
                <div class="edit-buttons">
                    <button class="btn-save" onclick="saveEdit(${caseId})">
                        <i class="bi bi-check"></i> 保存
                    </button>
                    <button class="btn-cancel" onclick="cancelEdit(${caseId}, '${escapedData}')">
                        <i class="bi bi-x"></i> 取消
                    </button>
                </div>
            `;

            // 替换显示内容为编辑区域
            displayDiv.style.display = 'none';
            editButton.style.display = 'none';
            cell.appendChild(editArea);
        }

    // 保存编辑 - 修改后的方法
    async function saveEdit(caseId) {
        const cell = document.querySelector(`tr:has(button[onclick="saveEdit(${caseId})"]) .case-data-cell`);
        const textarea = cell.querySelector('.edit-input');
        const editArea = cell.querySelector('div:has(.edit-input)');
        const displayDiv = cell.querySelector('.case-data-display');
        const editButton = cell.querySelector('.btn-edit');

        const newData = textarea.value;

        try {
            // 显示保存中状态
            const saveBtn = editArea.querySelector('.btn-save');
            const originalSaveText = saveBtn.innerHTML;
            saveBtn.innerHTML = '<i class="bi bi-arrow-clockwise spinner"></i> 保存中...';
            saveBtn.disabled = true;

            // 调用API保存数据
            await saveTestCase(caseId, newData);

            // 更新本地数据
            const caseIndex = window.originalTestCases.findIndex(c => c.id === caseId);
            if (caseIndex !== -1) {
                window.originalTestCases[caseIndex].caseData = newData;
                window.originalTestCases[caseIndex].updateTime = new Date().toLocaleString('zh-CN');
            }

            // 恢复显示模式
            displayDiv.textContent = newData;
            displayDiv.setAttribute('data-fulltext', newData);
            displayDiv.style.display = 'block';
            editButton.style.display = 'inline-block';
            editArea.remove();

            // 重新渲染表格以更新所有相关数据
            renderTestCases(window.originalTestCases);

            // 显示保存成功提示
            showToast('测试数据保存成功', 'success');

        } catch (error) {
            // 恢复按钮状态
            const saveBtn = editArea.querySelector('.btn-save');
            saveBtn.innerHTML = '<i class="bi bi-check"></i> 保存';
            saveBtn.disabled = false;

            // 显示错误提示
            showToast('保存失败: ' + error.message, 'danger');
            console.error('保存编辑失败:', error);
        }
    }

    // 取消编辑
    function cancelEdit(caseId, originalData) {
        const cell = document.querySelector(`tr:has(button[onclick="cancelEdit(${caseId}, '${originalData}')"]) .case-data-cell`);
        const editArea = cell.querySelector('div:has(.edit-input)');
        const displayDiv = cell.querySelector('.case-data-display');
        const editButton = cell.querySelector('.btn-edit');

        // 恢复显示模式
        displayDiv.style.display = 'block';
        editButton.style.display = 'inline-block';
        editArea.remove();

        showToast('编辑已取消', 'info');
    }

    // 保存测试用例数据到后端
    async function saveTestCase(caseId, caseData) {
        try {
            const response = await fetch(`/api/testcases/${caseId}`, {
                method: 'PUT',
                headers: {
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify({
                    caseData: caseData,
                    updateTime: new Date().toISOString()
                })
            });

            if (!response.ok) {
                throw new Error(`保存失败! status: ${response.status}`);
            }

            const result = await response.json();
            return result;
        } catch (error) {
            console.error('保存测试用例数据失败:', error);
            throw error;
        }
    }

    // 打开ID编辑模态框
    async function openEditIdModal(caseId) {
        try {
            // 显示加载状态
            showModalLoading();

            // 调用接口获取测试用例详情
            const testCase = await fetchTestCaseById(caseId);

            // 填充模态框数据
            document.getElementById('editIdCaseId').value = testCase.id;
            document.getElementById('editIdUrlId').value = testCase.urlId || ''; // 设置urlId
            document.getElementById('editIdTestItem').value = testCase.testItem || '';
            document.getElementById('editIdCaseName').value = testCase.caseName || '';
            document.getElementById('editIdStatus').value = testCase.status || 'PENDING';
            document.getElementById('editIdExpectCode').value = testCase.expectCode || '';
            document.getElementById('editIdActualCode').value = testCase.actualCode || '';
            document.getElementById('editIdResult').value = testCase.result || '';

            // 处理测试数据，如果是JSON则格式化
            let caseData = testCase.caseData || '';
            try {
                const jsonData = JSON.parse(caseData);
                caseData = JSON.stringify(jsonData, null, 2);
            } catch (e) {
                // 不是JSON，保持原样
            }
            document.getElementById('editIdCaseData').value = caseData;

            // 显示模态框
            const modal = new bootstrap.Modal(document.getElementById('editIdModal'));
            modal.show();

        } catch (error) {
            console.error('打开ID编辑模态框失败:', error);
            showToast('获取测试用例详情失败: ' + error.message, 'danger');
        }
    }

    // 获取测试用例详情接口
    async function fetchTestCaseById(caseId) {
        try {
            const response = await fetch(`/api/testcases/${caseId}`);
            if (!response.ok) {
                throw new Error(`HTTP error! status: ${response.status}`);
            }
            const result = await response.json();

            if (result.success) {
                return result.data;
            } else {
                throw new Error(result.message || '获取数据失败');
            }
        } catch (error) {
            console.error('获取测试用例详情失败:', error);
            throw error;
        }
    }

    // 保存ID编辑
    async function saveIdEdit() {
        const caseId = document.getElementById('editIdCaseId').value;
        const urlId = document.getElementById('editIdUrlId').value;

        // 收集表单数据
        const formData = {
            testItem: document.getElementById('editIdTestItem').value,
            caseName: document.getElementById('editIdCaseName').value,
            caseData: document.getElementById('editIdCaseData').value,
            status: document.getElementById('editIdStatus').value,
            expectCode: parseInt(document.getElementById('editIdExpectCode').value) || null,
            actualCode: parseInt(document.getElementById('editIdActualCode').value) || null,
            result: document.getElementById('editIdResult').value,
            urlId: urlId // 包含urlId
        };

        // 验证必填字段
        if (!formData.testItem || !formData.caseName) {
            showToast('请填写测试项和用例名称', 'warning');
            return;
        }

        try {
            // 显示保存中状态
            const saveBtn = document.querySelector('#editIdModal .btn-primary');
            const originalText = saveBtn.innerHTML;
            saveBtn.innerHTML = '<i class="bi bi-arrow-clockwise spinner"></i> 保存中...';
            saveBtn.disabled = true;

            // 调用保存接口
            await updateTestCaseById(caseId, formData);

            // 关闭模态框
            const modal = bootstrap.Modal.getInstance(document.getElementById('editIdModal'));
            modal.hide();

            // 刷新数据
            await refreshData();

            showToast('测试用例保存成功', 'success');

        } catch (error) {
            console.error('保存测试用例失败:', error);
            showToast('保存失败: ' + error.message, 'danger');

            // 恢复按钮状态
            const saveBtn = document.querySelector('#editIdModal .btn-primary');
            saveBtn.innerHTML = '<i class="bi bi-check"></i> 保存';
            saveBtn.disabled = false;
        }
    }

    // 更新测试用例接口
    async function updateTestCaseById(caseId, data) {
        try {
            const response = await fetch(`/api/testcases/${caseId}/update`, {
                method: 'PUT',
                headers: {
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify(data)
            });

            if (!response.ok) {
                throw new Error(`HTTP error! status: ${response.status}`);
            }

            const result = await response.json();

            if (!result.success) {
                throw new Error(result.message || '更新失败');
            }

            return result;
        } catch (error) {
            console.error('更新测试用例失败:', error);
            throw error;
        }
    }

    // 模态框中的JSON格式化
    function formatJsonInModal() {
        const textarea = document.getElementById('editIdCaseData');

        try {
            if (textarea.value.trim()) {
                const jsonObj = JSON.parse(textarea.value);
                const formattedJson = JSON.stringify(jsonObj, null, 2);
                textarea.value = formattedJson;
                showToast('JSON格式化成功', 'success');
            }
        } catch (error) {
            showToast('JSON格式错误，无法格式化', 'danger');
        }
    }

    // 显示模态框加载状态
    function showModalLoading() {
        // 可以在这里添加加载动画
        console.log('加载测试用例详情...');
    }

    // 更新统计信息
    function updateStats(cases) {
        const total = cases.length;
        const passed = cases.filter(c => c.status === 'PASS').length;
        const failed = cases.filter(c => c.status === 'FAIL').length;
        const blocked = cases.filter(c => c.status === 'BLOCKED').length;

        document.getElementById('totalCases').textContent = total;
        document.getElementById('passedCases').textContent = passed;
        document.getElementById('failedCases').textContent = failed;
        document.getElementById('blockedCases').textContent = blocked;
    }

    // 筛选表格函数
    function filterTable() {
        const statusFilter = document.getElementById('statusFilter').value;
        const itemFilter = document.getElementById('itemFilter').value;
        const searchInput = document.getElementById('searchInput').value.toLowerCase();

        let filteredCases = window.originalTestCases;

        // 应用状态筛选
        if (statusFilter !== 'all') {
            filteredCases = filteredCases.filter(c => c.status === statusFilter);
        }

        // 应用测试项筛选
        if (itemFilter !== 'all') {
            filteredCases = filteredCases.filter(c => c.testItem === itemFilter);
        }

        // 应用搜索筛选
        if (searchInput) {
            filteredCases = filteredCases.filter(c =>
                c.caseName.toLowerCase().includes(searchInput) ||
                c.caseData.toLowerCase().includes(searchInput) ||
                c.result.toLowerCase().includes(searchInput)
            );
        }

        renderTestCases(filteredCases);
        updateStats(filteredCases);
    }

    // 刷新数据
    async function refreshData() {
        // 显示加载状态
        document.getElementById('testCasesBody').innerHTML = `
            <tr>
                <td colspan="9" class="text-center py-5">
                    <div class="spinner-border text-primary" role="status">
                        <span class="visually-hidden">加载中...</span>
                    </div>
                    <p class="mt-3">正在刷新数据...</p>
                </td>
            </tr>
        `;

        // 重置筛选器
        document.getElementById('statusFilter').value = 'all';
        document.getElementById('itemFilter').value = 'all';
        document.getElementById('searchInput').value = '';

        // 获取jobId
        const jobId = getQueryParam('jobId');

        // 重新获取数据
        const testCases = await fetchTestCases(jobId);
        // 重新获取数据
        window.originalTestCases = testCases;

        renderTestCases(testCases);
        updateStats(testCases);
    }

    // 复制到剪贴板
    function copyToClipboard(text) {
        const textarea = document.createElement('textarea');
        textarea.value = text;
        document.body.appendChild(textarea);
        textarea.select();
        document.execCommand('copy');
        document.body.removeChild(textarea);
    }

    function showToast(message, type) {
        // 简单的toast实现
        const toast = document.createElement('div');
        toast.className = `alert alert-${type} alert-dismissible fade show position-fixed`;
        toast.style.cssText = 'top: 20px; right: 20px; z-index: 1050; min-width: 300px;';
        toast.innerHTML = `
            <i class="bi bi-check-circle"></i> ${message}
            <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
        `;
        document.body.appendChild(toast);

        setTimeout(() => {
            toast.remove();
        }, 1000);
    }
